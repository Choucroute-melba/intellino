package io.github.choucroutemelba.intellino.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ShortcutSet
import com.intellij.openapi.options.ShowSettingsUtil
import io.github.choucroutemelba.intellino.ui.dialog.ArduinoManagementDialog

class ArduinoManagementDialogAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        ArduinoManagementDialog().show()
    }

}

class OpenAppSettingsAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        ShowSettingsUtil.getInstance().showSettingsDialog(null, "Arduino")
    }
}


