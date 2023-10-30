package io.github.choucroutemelba.intellino.listener

import com.intellij.util.messages.Topic

val ARDUINO_APP_SERVICE_LISTENER_TOPIC = Topic("Arduino app service listener", ArduinoAppServiceListener::class.java)

interface ArduinoAppServiceListener {
    fun onEnvironmentChanged()
}