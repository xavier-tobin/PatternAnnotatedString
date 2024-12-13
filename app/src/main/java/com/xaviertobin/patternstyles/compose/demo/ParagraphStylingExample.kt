package com.xaviertobin.patternstyles.compose.demo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.xaviertobin.patternstyles.PatternAnnotation
import com.xaviertobin.patternstyles.compose.richAnnotated
import com.xaviertobin.patternstyles.drawParagraphBackgrounds
import com.xaviertobin.patternstyles.useParagraphBackgrounds
import java.util.regex.Pattern



/**
 * Custom paragraph styling requires using the base PatternStyle, not `basicPatternStyle`.
 */
val codeBlockStyle = PatternAnnotation(
    pattern = Pattern.compile(
        "```[^` ][^`]*[^ ]?```",
    ),
    spanStyle = {
        SpanStyle(
            fontFamily = FontFamily.Monospace,
        )
    },
    // Paragraph styling only supports modifying text positioning
    paragraphStyle = {
        ParagraphStyle(
            lineHeight = 2.em,
            lineHeightStyle = LineHeightStyle(
                LineHeightStyle.Alignment.Proportional,
                LineHeightStyle.Trim.LastLineBottom
            ),
            textIndent = TextIndent(
                firstLine = 10.sp,
                restLine = 10.sp
            ),
        )
    },
    drawParagraphBackground = { rect ->
        val fullWidthRect = rect.copy(
            right = size.width
        )
        drawRoundRect(
            color = Color.LightGray,
            topLeft = fullWidthRect.topLeft,
            size = fullWidthRect.size,
            cornerRadius = CornerRadius(
                10.dp.toPx(), 10.dp.toPx()
            )
        )
    }
)

val quoteBlockStyle = PatternAnnotation(
    pattern = Pattern.compile("(?<=^)(>.*\\n?)|(?<=\\n)(>.*\\n?)"),
    spanStyle = { SpanStyle(fontSize = 16.sp) },
    paragraphStyle = {
        ParagraphStyle(
            lineHeight = 2.em,
            lineBreak = LineBreak.Unspecified,
            textIndent = TextIndent(
                firstLine = 12.sp,
                restLine = 12.sp
            )
        )
    },
    drawParagraphBackground = { rect ->
        drawRoundRect(
            color = Color.LightGray,
            topLeft = rect.topLeft,
            size = Size(4.dp.toPx(), rect.height),
            cornerRadius = CornerRadius(
                10.dp.toPx(), 10.dp.toPx()
            )
        )
    }
)

@Composable
fun ParagraphStyling() {

    var textWithCodeBlock by remember { mutableStateOf( "Normal text\n```\ncode();\n```\nNormal text\n> Insert famous quote here") }
    val result = textWithCodeBlock.richAnnotated(
        patternAnnotations = listOf(codeBlockStyle, quoteBlockStyle)
    )
    val (backgrounds, onTextLayout) = useParagraphBackgrounds(
        paragraphBackgroundAnnotations = result.paragraphBackgroundAnnotations
    )

    OutlinedTextField(
        value = textWithCodeBlock,
        onValueChange = { textWithCodeBlock = it }
    )

    Text(
        text = result.annotatedString,
        modifier = Modifier
            .fillMaxWidth()
            .drawParagraphBackgrounds(backgrounds),
        onTextLayout = onTextLayout,
    )

}


@Preview
@Composable
fun ParagraphStylingTextPreview() {
    ScrollPreviewLayout {
        DemoSection("Paragraph styling") {
            ParagraphStyling()
        }
    }
}