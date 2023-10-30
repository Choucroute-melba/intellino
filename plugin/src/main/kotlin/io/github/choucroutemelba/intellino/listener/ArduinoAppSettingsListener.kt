package io.github.choucroutemelba.intellino.listener

import com.intellij.util.messages.Topic

val ARDUINO_APP_SETTINGS_TOPIC = Topic("Arduino app settings", ArduinoAppSettingsListener::class.java)
interface ArduinoAppSettingsListener {
    fun onSettingsChanged()
}