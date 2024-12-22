package com.xaviertobin.patternstyles

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
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
    val inlineContent: InlineContentFunction? = null,
    val linkAnnotationPlan: LinkAnnotationPlan? = null
)

data class LinkAnnotationPlan(
    val urlTagHandler: UrlTagHandler,
    val onClick: ((matchingText: String) -> Unit)? = null,
    val focusedStyle: SpanStyle? = null,
    val hoveredStyle: SpanStyle? = null,
    val pressedStyle: SpanStyle? = null
)

typealias InlineContentFunction = (text: String) -> InlineTextContent

typealias UrlTagHandler = (matchingString: String) -> String

typealias OnDrawBackground = DrawScope.(rect: Rect) -> Unit

data class MatchDetails(
    val start: Int,
    val end: Int,
    val group: String?
)

fun basicPatternAnnotation(
    pattern: String,
    literalPattern: Boolean = false,
    caseSensitive: Boolean = false,
    spanStyle: SpanStyle? = null,
): PatternAnnotation {
    return PatternAnnotation(
        pattern = Pattern.compile(
            pattern,
            (if (caseSensitive) 0 else Pattern.CASE_INSENSITIVE) or (if (literalPattern) Pattern.LITERAL else 0)
        ),
        spanStyle = if (spanStyle != null) {
            { spanStyle }
        } else null,
    )
}

fun inlineContentPatternAnnotation(
    pattern: String,
    caseSensitive: Boolean = false,
    inlineContent: InlineContentFunction,
): PatternAnnotation {
    return PatternAnnotation(
        pattern = Pattern.compile(pattern, if (caseSensitive) 0 else Pattern.CASE_INSENSITIVE),
        inlineContent = inlineContent
    )
}

fun paragraphPatternAnnotation(
    pattern: String,
    caseSensitive: Boolean = false,
    spanStyle: SpanStyle? = null,
    paragraphStyle: ParagraphStyle? = null,
    onDrawParagraphBackground: OnDrawBackground? = null,
): PatternAnnotation {
    return PatternAnnotation(
        pattern = Pattern.compile(pattern, if (caseSensitive) 0 else Pattern.CASE_INSENSITIVE),
        spanStyle = if (spanStyle != null) {
            { spanStyle }
        } else null,
        paragraphStyle = if (paragraphStyle != null) {
            { paragraphStyle }
        } else null,
        drawParagraphBackground = onDrawParagraphBackground
    )
}

fun linkPatternAnnotation(
    pattern: String,
    caseSensitive: Boolean = false,
    spanStyle: SpanStyle? = SpanStyle(textDecoration = TextDecoration.Underline),
    url: UrlTagHandler,
    focusedStyle: SpanStyle? = null,
    hoveredStyle: SpanStyle? = null,
    pressedStyle: SpanStyle? = null
): PatternAnnotation {
    return PatternAnnotation(
        pattern = Pattern.compile(pattern, if (caseSensitive) 0 else Pattern.CASE_INSENSITIVE),
        spanStyle = if (spanStyle != null) {
            { spanStyle }
        } else null,
        linkAnnotationPlan = LinkAnnotationPlan(
            urlTagHandler = url,
            focusedStyle = focusedStyle,
            hoveredStyle = hoveredStyle,
            pressedStyle = pressedStyle
        )
    )
}

fun clickablePatternAnnotation(
    pattern: String,
    caseSensitive: Boolean = false,
    spanStyle: SpanStyle? = SpanStyle(textDecoration = TextDecoration.Underline),
    onClick : (matchingText: String) -> Unit,
    focusedStyle: SpanStyle? = null,
    hoveredStyle: SpanStyle? = null,
    pressedStyle: SpanStyle? = null
): PatternAnnotation {
    return PatternAnnotation(
        pattern = Pattern.compile(pattern, if (caseSensitive) 0 else Pattern.CASE_INSENSITIVE),
        spanStyle = if (spanStyle != null) {
            { spanStyle }
        } else null,
        linkAnnotationPlan = LinkAnnotationPlan(
            urlTagHandler = { it },
            onClick = onClick,
            focusedStyle = focusedStyle,
            hoveredStyle = hoveredStyle,
            pressedStyle = pressedStyle
        )
    )
}