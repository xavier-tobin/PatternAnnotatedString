package com.xaviertobin.noted.compose.patternstyle.demo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.Preview
import com.xaviertobin.patternstyles.demo.DemoSection


val redFruitStyle = basicPatternStyle(
    pattern = "(\\w*berry)|(\\w{0,}apple)",
    spanStyle = SpanStyle(color = Color.Red)
)

@Composable
fun BasicExample() {

    val redFruit = rememberPatternAnnotatedString(
        text = "Book Strawberry Fridge Apple Ferrari",
        patternStyles = listOf(redFruitStyle)
    )

    Text(text = redFruit)
}


@Preview
@Composable
fun BasicStyledTextPreview() {
    ScrollPreviewLayout {
        DemoSection("Basic styling") {
            BasicExample()
        }
    }
}