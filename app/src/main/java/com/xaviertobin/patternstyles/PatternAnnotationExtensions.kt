package com.xaviertobin.patternstyles

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em


fun inlineContent(
    width: TextUnit = 1.em,
    height: TextUnit = 1.em,
    placeholderVerticalAlign: PlaceholderVerticalAlign = PlaceholderVerticalAlign.Center,
    content: @Composable (String) -> Unit
): InlineTextContent {
    return InlineTextContent(
        Placeholder(
            width = width,
            height = height,
            placeholderVerticalAlign = placeholderVerticalAlign
        ),
        children = content
    )

}
