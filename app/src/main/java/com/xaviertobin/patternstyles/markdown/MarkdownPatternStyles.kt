package com.xaviertobin.patternstyles.markdown

import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.xaviertobin.patternstyles.PatternStyle
import java.util.regex.Pattern


val QUOTE_BLOCK_PATTERN = PatternStyle(
    pattern = Pattern.compile("(?<=^)(>.*\\n?)|(?<=\\n)(>.*\\n?)"),
    spanStyle = { SpanStyle(fontSize = 16.sp) },
    paragraphStyle = {
        ParagraphStyle(
            lineHeight = 2.em,
            lineBreak = LineBreak.Unspecified,
            textIndent = TextIndent(
                firstLine = 20.sp,
                restLine = 20.sp
            )
        )
    },
    paragraphBackgroundTag = "quoteBlock"
)

val INLINE_CODE = PatternStyle(
    pattern = Pattern.compile("`[^`\\s]+`|(?<=[^`])``(?=[^`])"),
    spanStyle = {
        SpanStyle(
            fontFamily = FontFamily.Monospace
        )
    }
)

val CODE_BLOCK = PatternStyle(
    pattern = Pattern.compile(
        "```[^` ][^`]*[^ ]?```",
    ),
    spanStyle = {
        SpanStyle(
            fontFamily = FontFamily.Monospace
        )
    },
    paragraphStyle = {
        ParagraphStyle(
            lineBreak = LineBreak.Unspecified,
            textIndent = TextIndent(
                firstLine = 10.sp,
                restLine = 10.sp
            ),
            lineHeight = 2.em,
            lineHeightStyle = LineHeightStyle(
                LineHeightStyle.Alignment.Proportional,
                LineHeightStyle.Trim.LastLineBottom
            )
        )
    },
    paragraphBackgroundTag = "codeBlock"
)

val HEADERS = PatternStyle(
    pattern = Pattern.compile("^(#{1,6}) .*$", Pattern.MULTILINE),
    spanStyle = {
        SpanStyle(
            fontWeight = FontWeight.Bold,
            fontSize = (34 - ((it.group?.length  ?: 0))* 3).sp
        )
    }
)

val markdownPatternStyles = listOf(
    QUOTE_BLOCK_PATTERN,
    INLINE_CODE,
    CODE_BLOCK,
    HEADERS
)