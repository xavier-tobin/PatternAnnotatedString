package com.xaviertobin.patternstyles.compose.demo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.Preview
import com.xaviertobin.patternstyles.basicPatternAnnotation
import com.xaviertobin.patternstyles.compose.annotated


val redFruitStyle = basicPatternAnnotation(
    pattern = "(\\w*berry)|(\\w{0,}apple)",
    spanStyle = SpanStyle(color = Color.Red)
)

@Composable
fun BasicExample() {

    val plainText = "Book Strawberry Fridge Apple Ferrari"

    val redFruit = plainText.annotated(
        patternAnnotations = listOf(redFruitStyle)
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