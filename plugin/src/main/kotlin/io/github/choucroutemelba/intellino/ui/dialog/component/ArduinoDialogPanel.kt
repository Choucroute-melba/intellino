package io.github.choucroutemelba.intellino.ui.dialog.component

import com.intellij.openapi.Disposable
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.tabs.JBTabsFactory
import com.intellij.ui.tabs.TabInfo
import io.github.choucroutemelba.intellino.service.ArduinoApplicationService
import io.github.choucroutemelba.intellino.settings.ArduinoAppSettingsState
import io.github.choucroutemelba.intellino.ui.dialog.ArduinoManagementDialogUiState
import io.github.choucroutemelba.intellino.ui.utils.folderLink
import java.awt.Container
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JPanel

class ArduinoDialogPanel(val uiState: ArduinoManagementDialogUiState, val disposable: Disposable): JFrame() {

    init {
        rootPane.contentPane = contentPane
    }

    override fun getContentPane(): Container {
        return panel {
            row {
                label(uiState.updateCount.toString())
                if(uiState.appService.cliInstalled) {
                    label(uiState.settingsState.arduinoCliPath!!)
                } else {
                    label("It seems like Arduino CLI is not installed, please see available options in settings.")
                    button("Open Settings") {
                        ShowSettingsUtil.getInstance().showSettingsDialog(null, "Arduino")
                    }
                }
            }
            row { cell(ManagementTabs(uiState.appService, uiState.settingsState, disposable)) }
        }
    }

    fun update() {
        rootPane.contentPane = contentPane
    }
}

fun ManagementTabs(appService: ArduinoApplicationService, settingsState: ArduinoAppSettingsState, disposable: Disposable): JComponent {
    return JBTabsFactory.createTabs(null, disposable).apply {
            addTab(TabInfo(
                librariesPanel(appService.dataFolder)
            ).setText("Libraries"), 0)

            addTab(TabInfo(
                boardPanel(appService.dataFolder)
            ).setText("Boards"), 1)

            addTab(TabInfo(
                sketchesPanel(appService.userFolder)
            ).setText("Sketches"), 2)

            addTab(TabInfo(
                debugInfoPanel(appService, settingsState)
            ).setText("Debug"), 3)
        }.component
}



fun librariesPanel(dataFolder: String?): JPanel {
            return panel {
                row {
                    label("Libraries")
                    cell(
                        folderLink(dataFolder ?: "./",
                        dataFolder ?: "User folder is not set, please verify your arduino installation.")
                    )
                    if(dataFolder == null) {
                        link("Open settings") {
                            ShowSettingsUtil.getInstance().showSettingsDialog(null, "Arduino")
                        }
                    }
                }
            }
}

fun boardPanel(dataFolder: String?) : JPanel {
    return panel {
            row {
                label("Boards")
                cell(folderLink(dataFolder ?: "./",
                    dataFolder ?: "User folder is not set, please verify your arduino installation."))
                if(dataFolder == null) {
                    link("Open settings") {
                        ShowSettingsUtil.getInstance().showSettingsDialog(null, "Arduino")
                    }
                }
            }
        }
}

fun sketchesPanel(userFolder: String?): JPanel {
    return panel {
            row {
                label("Sketches")
                cell(folderLink(userFolder ?: "./",
                    userFolder ?: "User folder is not set, please verify your arduino installation."))
                if(userFolder == null) {
                    link("Open settings") {
                        ShowSettingsUtil.getInstance().showSettingsDialog(null, "Arduino")
                    }
                }
            }
        }
}

fun debugInfoPanel(appService: ArduinoApplicationService, settingsState: ArduinoAppSettingsState): JPanel {
    return panel {
            row("Cli path from settings : ${settingsState.arduinoCliPath ?: "Arduino CLI path is null"}") {}
            row("Cli installed : ${appService.cliInstalled}") {}
            row("Cli path : ${appService.arduinoCliPath ?: " Arduino CLI path is null"}") {}
        }
}