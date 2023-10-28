package io.github.choucroutemelba.intellino.ui.dialog

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.service
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.profiler.actions.dataContextBasedCopyFrameAction
import io.github.choucroutemelba.intellino.action.OpenAppSettingsAction
import io.github.choucroutemelba.intellino.listener.ARDUINO_APP_SERVICE_LISTENER_TOPIC
import io.github.choucroutemelba.intellino.listener.ARDUINO_APP_SETTINGS_TOPIC
import io.github.choucroutemelba.intellino.listener.ArduinoAppServiceListener
import io.github.choucroutemelba.intellino.listener.ArduinoAppSettingsListener
import io.github.choucroutemelba.intellino.service.ArduinoApplicationService
import io.github.choucroutemelba.intellino.settings.ArduinoAppSettingsState
import io.github.choucroutemelba.intellino.ui.dialog.component.ArduinoDialogPanel
import javax.swing.Action
import javax.swing.JComponent

class ArduinoManagementDialogUiState (
    var updateCount: Int,
    var appService: ArduinoApplicationService,
    var settingsState: ArduinoAppSettingsState
    )
class ArduinoManagementDialog : DialogWrapper(true) {

    private var myCenterPanel: ArduinoDialogPanel? = null
    private var msgBusConnection = ApplicationManager.getApplication().messageBus.connect()

    private val uiState = ArduinoManagementDialogUiState (
        updateCount = 0,
        appService = ArduinoApplicationService.getInstance(),
        settingsState = ArduinoAppSettingsState.getInstance()
    )
    init {
        init()
        println("ArduinoManagementDialog.init()")
        title = "Arduino Managment"
        msgBusConnection.subscribe(ARDUINO_APP_SETTINGS_TOPIC, SettingsListener(this))
        msgBusConnection.subscribe(ARDUINO_APP_SERVICE_LISTENER_TOPIC, AppServiceListener(this))
    }

    override fun createCenterPanel(): JComponent {
        myCenterPanel = makeCenterPanel()
        return myCenterPanel!!.rootPane
    }

    fun updateCenterPanel() {
        if(myCenterPanel == null) return
        uiState.updateCount++
        uiState.appService = service<ArduinoApplicationService>()
        myCenterPanel!!.update()
    }

    override fun dispose() {
        super.dispose()
        msgBusConnection.disconnect()
        msgBusConnection.dispose()
    }

    private fun makeCenterPanel(): ArduinoDialogPanel {
        uiState.appService = service<ArduinoApplicationService>()

        return ArduinoDialogPanel(uiState, disposable)
    }

    override fun createLeftSideActions(): Array<Action> {
        val openSettingsAction = object: DialogWrapperAction("Open Settings") {
            override fun doAction(e: java.awt.event.ActionEvent?) {
                ShowSettingsUtil.getInstance().showSettingsDialog(null, "Arduino")
            }
        }
        return arrayOf(
            openSettingsAction
        )
    }

    companion object {
        class SettingsListener(private val instance: ArduinoManagementDialog) : ArduinoAppSettingsListener {
            override fun onSettingsChanged() {
                println("ArduinoManagementDialog.Listener.onSettingsChanged() - update center panel")
                instance.updateCenterPanel()
            }
        }

        class AppServiceListener(private val instance: ArduinoManagementDialog): ArduinoAppServiceListener {
            override fun onEnvironmentChanged() {
                println("ArduinoManagementDialog.Listener.onEnvironmentChanged() - update center panel")
                instance.updateCenterPanel()
            }
        }
    }
}