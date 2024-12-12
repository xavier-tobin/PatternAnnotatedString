package com.xaviertobin.patternstyles.demo

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.em
import com.xaviertobin.patternstyles.basicPatternStyle
import com.xaviertobin.patternstyles.rememberRichPatternAnnotatedString


val usernamePatternStyle = basicPatternStyle(
    pattern = "@[A-Za-z0-9_]+",
    inlineContentTag = "username",
)

@Composable
fun CustomComposableExample() {

    val highlightedFruit = rememberRichPatternAnnotatedString(
        text = "Carrot \uD83C\uDF45 Tomato Apple Potato",
        patternStyles = listOf(usernamePatternStyle)
    )

    val inlineContent = mapOf(
        "username" to emojiInlineContent("🍅")
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