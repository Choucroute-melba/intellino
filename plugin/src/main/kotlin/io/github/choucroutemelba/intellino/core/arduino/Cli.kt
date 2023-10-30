package io.github.choucroutemelba.intellino.core.arduino

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.intellij.execution.configurations.GeneralCommandLine
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Class to execute arduino-cli commands
 *
 * @property cliExecutable The path to the arduino-cli executable (default: "arduino-cli")
 * @property outputFormat The output format of the arduino-cli command (default: "json") (can be "json" or "text")
 * @property configFile The path to the arduino-cli config file (default: null). if null, the cli will use the default config file
 * @property workingDirectory The working directory of the arduino-cli command (default: ".\\") (default: current directory)
 *
 */
class Cli(
    var cliExecutable: String
) {
    var outputFormat: String = "json"
    var configFile: String? = null
    var workingDirectory: String = ".\\"

    /**
     * Execute an arduino-cli command
     * @param arg The arguments of the command
     * @return [String] The output of the command
     * @throws CliException If the command fails
     */
    fun executeCommand(vararg arg: String): String {
        val command = mutableListOf(cliExecutable, *arg)
        if(configFile != null) {
            command.add("--config-file")
            command.add(configFile!!)
        }
        if(!command.contains("--format")) {
            command.add("--format")
            command.add(outputFormat)
        }

        try {
            println("Executing command: $command")
            val processBuilder = GeneralCommandLine(command)
            processBuilder.workDirectory = java.io.File(workingDirectory)

            val process = processBuilder.createProcess()
            val reader = BufferedReader(InputStreamReader(process.inputStream))

            process.waitFor()

            if(process.exitValue() == 0)
                return reader.readText()
            else {
                throw CliException("Error executing command: $command : Exit value ${process.exitValue()}", process.exitValue(), reader.readText())
            }
        } catch (e: Exception) {
            println("Error executing command: $command")
            println(e.message)
            throw CliException("Error executing command: $command\n${e.message}")
        }
    }

    /**
     * Execute an arduino-cli command and parse the output as json
     * @param arg The arguments of the command
     * @return [JsonObject] The output of the command as a json object
     * @throws CliException If the command fails
     */
    fun executeCommandForJson(vararg arg: String): JsonObject {
        if (outputFormat != "json") {
            println("Info: output format is not json, switching to json")
            outputFormat = "json"
        }
        val output = executeCommand(*arg)
        try {
            return JsonParser.parseString(output).asJsonObject
        } catch (e: JsonSyntaxException) {
            throw CliException("Syntax error while parsing json output : \n${e.message}", output = output)
        } catch (e: Exception) {
            throw CliException("Error parsing json output : \n${e.message}", output = output)
        }
    }
}

/**
 * Exception thrown when an arduino-cli command fails
 * @param message The error message with details of the run plus the exception message
 * @param output The output of the command if any (default: null)
 * @param exitValue The exit value of the command if any (default: null)
 */
class CliException(message: String, exitValue: Int? = null, output: String? = null): Exception(message)