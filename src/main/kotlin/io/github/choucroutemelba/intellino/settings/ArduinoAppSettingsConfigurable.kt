package io.github.choucroutemelba.intellino.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.Configurable
import io.github.choucroutemelba.intellino.listener.ARDUINO_APP_SETTINGS_TOPIC
import javax.swing.JComponent

class ArduinoAppSettingsConfigurable: Configurable {
    private lateinit var settingsComponent: ArduinoAppSettingsComponent
    override fun createComponent(): JComponent {
        settingsComponent = ArduinoAppSettingsComponent(ArduinoAppSettingsState.getInstance())
        return settingsComponent.getPanel()
    }

    override fun isModified(): Boolean {
        val settings = ArduinoAppSettingsState.getInstance()
        return (
                settings.arduinoCliPath != settingsComponent.getCliExecutable()
                )
    }

    override fun apply() {
        val settings = ArduinoAppSettingsState.getInstance()
        settings.arduinoCliPath = settingsComponent.getCliExecutable()
        ApplicationManager.getApplication().messageBus.syncPublisher(ARDUINO_APP_SETTINGS_TOPIC).onSettingsChanged()
    }

    override fun getDisplayName(): String {
        return "Arduino"
    }

}