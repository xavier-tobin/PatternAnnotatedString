package com.xaviertobin.patternstyles

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import java.util.regex.Pattern

/**
 * Describes a strategy to style a given pattern.
 */
data class PatternStyle(
    val pattern: Pattern,
    val spanStyle: ((details: MatchDetails) -> SpanStyle)? = null,
    val paragraphStyle: ((details: MatchDetails) -> ParagraphStyle)? = null,
    val inlineContentTag: String? = null,
    val paragraphBackgroundTag: String? = null,
    val drawParagraphBackground: (DrawScope.() -> Unit)? = null
)

data class MatchDetails(
    val start: Int,
    val end: Int,
    val group: String?
)

fun basicPatternStyle(
    pattern: String,
    caseSensitive: Boolean = false,
    spanStyle: SpanStyle? = null,
    paragraphStyle: ParagraphStyle? = null,
    inlineContentTag: String? = null
): PatternStyle {
    return PatternStyle(
        pattern = Pattern.compile(pattern, if (caseSensitive) 0 else Pattern.CASE_INSENSITIVE),
        spanStyle = if (spanStyle != null) { { spanStyle } } else null,
        paragraphStyle = if (paragraphStyle != null) { { paragraphStyle } } else null,
        inlineContentTag = inlineContentTag
    )
}