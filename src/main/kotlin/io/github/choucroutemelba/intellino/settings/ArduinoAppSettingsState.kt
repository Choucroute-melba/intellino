package io.github.choucroutemelba.intellino.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil
import io.github.choucroutemelba.intellino.service.ArduinoApplicationService

@State(name = "io.github.choucroutemelba.intellino.settings.ArduinoAppSettingsState",
    storages = [Storage("intellinoAppSettings.xml")])
class ArduinoAppSettingsState: PersistentStateComponent<ArduinoAppSettingsState> {
    var arduinoCliPath: String? = null

    init {
        println("ArduinoAppSettingsState init - $this")
    }
    override fun getState(): ArduinoAppSettingsState {
        return this
    }

    override fun loadState(state: ArduinoAppSettingsState) {
        println("Loading app settings state")
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): ArduinoAppSettingsState {
            return service()
        }
    }
}