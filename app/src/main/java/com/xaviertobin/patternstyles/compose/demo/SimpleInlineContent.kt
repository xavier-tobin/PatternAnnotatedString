package com.xaviertobin.patternstyles.compose.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.xaviertobin.patternstyles.PatternAnnotation
import com.xaviertobin.patternstyles.compose.annotatedRich
import com.xaviertobin.patternstyles.simpleInlineTextContent
import java.util.regex.Pattern

val usernameMap = mapOf(
    "@xavier" to "Xavier Tobin",
)

val usernameAnnotation = PatternAnnotation(
    pattern = Pattern.compile("@[A-Za-z0-9_]+"),
    inlineContent = { text -> usernamePill(username = text) },
)

fun usernamePill(username: String): InlineTextContent {
    return simpleInlineTextContent(width = 7.3.em, height = 1.8.em) {

        val actualName = usernameMap[username] ?: username

        Text(
            actualName,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(2.dp)
                .background(
                    color = Color.Green.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 10.dp, vertical = 2.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun SimpleInlineExample() {

    val highlightedFruit = "Thanks for making this library @xavier, it's cool!".annotatedRich(
        patternAnnotations = listOf(usernameAnnotation)
    )

    Text(
        text = highlightedFruit.annotatedString,
        inlineContent = highlightedFruit.inlineContentMap
    )
}


@Preview
@Composable
fun SimpleInlineExamplePreview() {
    ScrollPreviewLayout {
        DemoSection("Custom composable") {
            SimpleInlineExample()
        }
    }
}