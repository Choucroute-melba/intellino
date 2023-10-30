package io.github.choucroutemelba.intellino.service

import com.intellij.openapi.components.service
import io.github.choucroutemelba.intellino.listener.ArduinoAppSettingsListener

interface ArduinoApplicationService {
    val helloWorld: String
    var cliInstalled: Boolean
    var dataFolder: String?
    var userFolder: String?
    var arduinoCliPath: String?

    fun retrieveDataFolder(): String?
    fun retrieveUserFolder(): String?
    fun retrieveCliPath(): String?
    fun onAppSettingsChanged()

    companion object {
        fun getInstance(): ArduinoApplicationService {
            return service()
        }
        object Listener : ArduinoAppSettingsListener {
            override fun onSettingsChanged() {
                getInstance().onAppSettingsChanged()
            }
        }
    }
}