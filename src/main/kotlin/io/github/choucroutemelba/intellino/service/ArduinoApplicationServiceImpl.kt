package io.github.choucroutemelba.intellino.service

import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.messages.Topic
import io.github.choucroutemelba.intellino.core.arduino.Cli
import io.github.choucroutemelba.intellino.core.arduino.CliException
import io.github.choucroutemelba.intellino.listener.ARDUINO_APP_SERVICE_LISTENER_TOPIC
import io.github.choucroutemelba.intellino.settings.ArduinoAppSettingsState

class ArduinoApplicationServiceImpl: ArduinoApplicationService {
    override val helloWorld: String = "Hello Arduino World!"

    override var cliInstalled: Boolean = false
    override var dataFolder: String? = null
    override var userFolder: String? = null
    override var arduinoCliPath: String? = null

    private var cli: Cli? = null

    init {
        val settingsState = ArduinoAppSettingsState.getInstance()
        if(settingsState.arduinoCliPath != null) {
            arduinoCliPath = settingsState.arduinoCliPath
            cli = Cli(arduinoCliPath!!)
        }
        dataFolder = retrieveDataFolder()
        userFolder = retrieveUserFolder()
        if(dataFolder != null && userFolder != null) {
            cliInstalled = true
        }
    }

    override fun retrieveDataFolder(): String? {
        if(cli == null) return null
        return try {
            val result = cli!!.executeCommandForJson("config", "dump")
            result.get("directories").asJsonObject.get("data").asString
        } catch (e: CliException) {
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun retrieveUserFolder(): String? {
        if(cli == null) return null
        return try {
            val result = cli!!.executeCommandForJson("config", "dump")
            result.get("directories").asJsonObject.get("user").asString
        } catch (e: CliException) {
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun retrieveCliPath(): String? {
        TODO("Not yet implemented")
    }

    override fun onAppSettingsChanged() { // TODO: Avoid UI freeze
        val settingsState = ArduinoAppSettingsState.getInstance()
        if(settingsState.arduinoCliPath != null) {
            arduinoCliPath = settingsState.arduinoCliPath
            cli = Cli(arduinoCliPath!!)
        }
        dataFolder = retrieveDataFolder()
        userFolder = retrieveUserFolder()
        cliInstalled = dataFolder != null && userFolder != null
        ApplicationManager.getApplication().messageBus.syncPublisher(ARDUINO_APP_SERVICE_LISTENER_TOPIC).onEnvironmentChanged()
    }

    init {
        println("initialising ArduinoApplicationServiceImpl")
    }
}
