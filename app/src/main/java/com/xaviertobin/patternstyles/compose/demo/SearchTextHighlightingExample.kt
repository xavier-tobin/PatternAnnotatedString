package com.xaviertobin.patternstyles.compose.demo

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xaviertobin.patternstyles.basicPatternAnnotation
import com.xaviertobin.patternstyles.compose.annotated


const val LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
        "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
        "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
        "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
        "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

@Composable
fun SearchTextHighlighting() {

    var searchQuery by remember { mutableStateOf("") }

    val highlightStyle = basicPatternAnnotation(
        pattern = searchQuery,
        spanStyle = SpanStyle(
            background = Color.Yellow,
            fontWeight = FontWeight.Black
        )
    )

    val highlightedText = LOREM_IPSUM.annotated(
        patternAnnotations = listOf(highlightStyle)
    )

    TextField(
        value = searchQuery,
        onValueChange = { searchQuery = it }
    )

    Text(
        text = highlightedText,
        modifier = Modifier.padding(top = 10.dp)
    )
}

@Preview
@Composable
fun SearchTextHighlightingPreview() {
    ScrollPreviewLayout {
        DemoSection("Search highlighting") {
            SearchTextHighlighting()
        }
    }
}