package io.github.choucroutemelba.intellino.settings

import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.text
import javax.swing.JPanel

class ArduinoAppSettingsComponent(
    val settingsState: ArduinoAppSettingsState
) {
    private var cliExecutable: String? = settingsState.arduinoCliPath

    private val mainPanel: JPanel = panel {
        indent {
            row("Arduino CLI Configuration") {}
            row {
                label("Arduino CLI executable:")
                textFieldWithBrowseButton("Select Arduino CLI executable") {
                    cliExecutable = it.path
                    return@textFieldWithBrowseButton it.path
                }.onChanged { cliExecutable = it.text }
                    .text(cliExecutable ?: "")
            }
        }
    }

    fun getPanel(): JPanel {
        return mainPanel
    }

    fun getCliExecutable(): String? {
        return cliExecutable
    }
    fun setCliExecutable(cliExecutable: String?) {
        this.cliExecutable = cliExecutable
    }
}