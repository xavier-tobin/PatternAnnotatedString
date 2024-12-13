package com.xaviertobin.patternstyles

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import java.util.regex.Pattern

/**
 * Describes a strategy to style a given pattern.
 */
data class PatternAnnotation(
    val pattern: Pattern,
    val spanStyle: ((details: MatchDetails) -> SpanStyle)? = null,
    val paragraphStyle: ((details: MatchDetails) -> ParagraphStyle)? = null,
    val inlineContentTag: String? = null,
    val drawParagraphBackground: OnDrawBackground? = null,
    val inlineContent: InlineContentFunction? = null
)

typealias InlineContentFunction = (text: String) -> InlineTextContent

typealias OnDrawBackground = DrawScope.(rect: Rect) -> Unit

data class MatchDetails(
    val start: Int,
    val end: Int,
    val group: String?
)

fun basicPatternAnnotation(
    pattern: String,
    caseSensitive: Boolean = false,
    spanStyle: SpanStyle? = null,
    paragraphStyle: ParagraphStyle? = null,
    inlineContentTag: String? = null,
): PatternAnnotation {
    return PatternAnnotation(
        pattern = Pattern.compile(pattern, if (caseSensitive) 0 else Pattern.CASE_INSENSITIVE),
        spanStyle = if (spanStyle != null) { { spanStyle } } else null,
        paragraphStyle = if (paragraphStyle != null) { { paragraphStyle } } else null,
        inlineContentTag = inlineContentTag
    )
}