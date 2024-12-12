package com.xaviertobin.patternstyles.demo

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xaviertobin.noted.compose.components.core.ScrollPreviewLayout
import com.xaviertobin.noted.compose.core.theme.elevatedSurface
import com.xaviertobin.noted.compose.patternstyle.demo.BasicExample
import com.xaviertobin.patternstyles.basicPatternStyle
import com.xaviertobin.patternstyles.rememberPatternAnnotatedString


const val LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
        "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
        "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
        "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
        "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

@Composable
fun SearchTextHighlighting() {

    var searchQuery by remember { mutableStateOf("") }

    val highlightStyle = basicPatternStyle(
        pattern = searchQuery,
        spanStyle = SpanStyle(
            background = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Black
        )
    )

    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it }
    )

    val highlightedText =  rememberPatternAnnotatedString(
        text = LOREM_IPSUM,
        patternStyles = listOf(highlightStyle)
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
        DemoSection("Basic styling") {
            BasicExample()
        }
    }
}