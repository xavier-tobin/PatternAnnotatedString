package com.xaviertobin.patternannotatedstring.compose.demo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.xaviertobin.patternannotatedstring.basicPatternAnnotation
import com.xaviertobin.patternannotatedstring.inlineContentPatternAnnotation
import com.xaviertobin.patternannotatedstring.inlineTextContent
import com.xaviertobin.patternannotatedstring.linkPatternAnnotation
import com.xaviertobin.patternannotatedstring.paragraphPatternAnnotation


/**
 * # Basic italics example
 */

val italicsMarkdown = basicPatternAnnotation(
    pattern = "_.*?_",
    spanStyle = SpanStyle(fontStyle = FontStyle.Italic),
)

val redFruit = basicPatternAnnotation(
    pattern = "(\\w*berry)|(\\w{0,}apple)",
    spanStyle = SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold)
)

val linkAnnotation = linkPatternAnnotation(
    pattern = "Bundled Notes",
    url = "https://bundlednotes.com"
)

val autoLinkAnnotation = linkPatternAnnotation(
    pattern = "https?://[^ ]+",
    url = { it }
)

val emailMailToLinkAnnotation = linkPatternAnnotation(
    pattern = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}",
    url = { "mailto:$it" }
)

val rightAlignedAnnotation = paragraphPatternAnnotation(
    pattern = "\\(.+\\)",
    paragraphStyle = ParagraphStyle(
        textAlign = TextAlign.End
    )
)

val codeBlockAnnotation = paragraphPatternAnnotation(
    pattern = "```[^` ][^`]*[^ ]?```",
    spanStyle = SpanStyle(
        fontFamily = FontFamily.Monospace,
    ),
    paragraphStyle = ParagraphStyle(
        // The code block looks better with these paragraph settings
        lineHeight = 2.em,
        lineHeightStyle = LineHeightStyle(
            LineHeightStyle.Alignment.Proportional,
            LineHeightStyle.Trim.LastLineBottom
        ),
        textIndent = TextIndent(
            firstLine = 10.sp,
            restLine = 10.sp
        ),
    ),
    onDrawParagraphBackground = { rect ->
        // You can draw pretty much anything here, but...
        // This runs on the main thread, so keep it simple
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

val usernameAnnotation = inlineContentPatternAnnotation(
    pattern = "@[A-Za-z0-9_]+",
    inlineContent = { matchedText ->
        inlineTextContent(width = 6.8.em, height = 1.4.em) {
            // You can use any composable here, but stick within the above bounds^
            Pill(usernameToNameMap[matchedText] ?: matchedText)
        }
    }
)

val checkBoxAnnotation = inlineContentPatternAnnotation(
    pattern = "^(_ )",
    inlineContent = {
        inlineTextContent(
            width = 16.sp,
            height = 16.sp,
        ) {
            // You can replace this with any Composable you want
            Canvas(modifier = Modifier.size(16.dp)) {
                drawCircle(
                    color = Color.Gray,
                    radius = size.height
                )
            }
        }
    }
)
