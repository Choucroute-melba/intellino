package io.github.choucroutemelba.intellino.ui.utils

import com.intellij.ui.components.ActionLink
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.CellBase
import com.intellij.ui.dsl.builder.Row
import java.awt.Desktop
import java.io.File

public fun folderLink(path: String, text: String = path): ActionLink {
    return ActionLink(text, {
        Desktop.getDesktop().open(File(path))
    })
}