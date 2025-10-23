package com.xaviertobin.patternannotatedstring

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.TextUnit


fun inlineTextContent(
    width: TextUnit,
    height: TextUnit,
    placeholderVerticalAlign: PlaceholderVerticalAlign = PlaceholderVerticalAlign.Bottom,
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
