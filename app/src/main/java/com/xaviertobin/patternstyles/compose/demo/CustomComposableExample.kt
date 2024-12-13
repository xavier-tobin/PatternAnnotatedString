package com.xaviertobin.patternstyles.compose.demo

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.em
import com.xaviertobin.patternstyles.basicPatternAnnotation
import com.xaviertobin.patternstyles.compose.richAnnotated


val manualInlineContent = basicPatternAnnotation(
    pattern = "tomato",
    inlineContentTag = "username",
)

@Composable
fun CustomComposableExample() {

    val highlightedFruit = "I LOVE tomatoes".richAnnotated(
        patternAnnotations = listOf(manualInlineContent)
    )

    val inlineContent = mapOf(
        "tomato" to emojiInlineContent("🍅")
    )

    Text(text = highlightedFruit.annotatedString, inlineContent = inlineContent)
}

@Composable
fun emojiInlineContent(text: String): InlineTextContent {
    return InlineTextContent(
        Placeholder(
            width = 1.15.em,
            height = 1.15.em,
            placeholderVerticalAlign = PlaceholderVerticalAlign.Center
        )
    ) {
        Text(text)
    }
}




@Preview
@Composable
fun CustomComposableExamplePreview() {
    ScrollPreviewLayout {
        DemoSection("Custom composable") {
            CustomComposableExample()
        }
    }
}