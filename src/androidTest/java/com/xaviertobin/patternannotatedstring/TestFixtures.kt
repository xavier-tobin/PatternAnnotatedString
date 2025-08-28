package com.xaviertobin.patternannotatedstring

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.xaviertobin.patternannotatedstring.compose.demo.Pill
import com.xaviertobin.patternannotatedstring.compose.demo.usernameToNameMap

object TestFixtures {
    val italicsMarkdown = basicPatternAnnotation(
        pattern = "_.*?_",
        spanStyle = SpanStyle(fontStyle = FontStyle.Italic),
    )

     val boldMarkdown = basicPatternAnnotation(
        pattern = "\\*\\*.*?\\*\\*",
        spanStyle = SpanStyle(fontWeight = FontWeight.Bold),
    )

     val rightAlignedAnnotation = paragraphPatternAnnotation(
        pattern = "->.+$",
        paragraphStyle = ParagraphStyle(
            textAlign = TextAlign.End
        )
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

     val codeBlockAnnotation = paragraphPatternAnnotation(
        pattern = "```[^` ][^`]*[^ ]?```",
        spanStyle = SpanStyle(
            fontFamily = FontFamily.Monospace,
        ),
        paragraphStyle = ParagraphStyle(
            lineHeight = 2.em,
            textIndent = TextIndent(
                firstLine = 10.sp,
                restLine = 10.sp
            ),
        ),
        onDrawParagraphBackground = { rect ->
            drawRoundRect(
                color = Color.LightGray,
                topLeft = rect.topLeft,
                size = rect.size,
                cornerRadius = CornerRadius(
                    10.dp.toPx(), 10.dp.toPx()
                )
            )
        }
    )

    val autoLinkAnnotation = linkPatternAnnotation(
        pattern = "https?://[^ ]+",
        url = { it }
    )

}