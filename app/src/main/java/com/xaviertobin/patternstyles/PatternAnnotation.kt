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

internal fun buildPatternAnnotation(
    pattern: String,
    caseSensitive: Boolean = false,
    literalPattern: Boolean = false,
    spanStyle: SpanStyle? = null,
    paragraphStyle: ParagraphStyle? = null,
    inlineContentTag: String? = null,
    drawParagraphBackground: OnDrawBackground? = null,
    inlineContent: InlineContentFunction? = null,
    linkAnnotationPlan: LinkAnnotationPlan? = null
): PatternAnnotation {
    return PatternAnnotation(
        pattern = Pattern.compile(
            pattern,
            (if (caseSensitive) 0 else Pattern.CASE_INSENSITIVE) or (if (literalPattern) Pattern.LITERAL else 0)
        ),
        spanStyle = if (spanStyle != null) {
            { spanStyle }
        } else null,
        paragraphStyle = if (paragraphStyle != null) {
            { paragraphStyle }
        } else null,
        inlineContentTag = inlineContentTag,
        drawParagraphBackground = drawParagraphBackground,
        inlineContent = inlineContent,
        linkAnnotationPlan = linkAnnotationPlan
    )
}


fun basicPatternAnnotation(
    pattern: String,
    literalPattern: Boolean = false,
    caseSensitive: Boolean = false,
    spanStyle: SpanStyle? = null,
) = buildPatternAnnotation(
    pattern = pattern,
    caseSensitive = caseSensitive,
    literalPattern = literalPattern,
    spanStyle = spanStyle
)


fun inlineContentPatternAnnotation(
    pattern: String,
    caseSensitive: Boolean = false,
    inlineContent: InlineContentFunction,
) = buildPatternAnnotation(
    pattern = pattern,
    caseSensitive = caseSensitive,
    inlineContent = inlineContent
)

fun paragraphPatternAnnotation(
    pattern: String,
    caseSensitive: Boolean = false,
    spanStyle: SpanStyle? = null,
    paragraphStyle: ParagraphStyle? = null,
    onDrawParagraphBackground: OnDrawBackground? = null,
) = buildPatternAnnotation(
    pattern = pattern,
    caseSensitive = caseSensitive,
    spanStyle = spanStyle,
    paragraphStyle = paragraphStyle,
    drawParagraphBackground = onDrawParagraphBackground
)

fun linkPatternAnnotation(
    pattern: String,
    caseSensitive: Boolean = false,
    spanStyle: SpanStyle? = SpanStyle(textDecoration = TextDecoration.Underline),
    url: UrlTagHandler,
    focusedStyle: SpanStyle? = null,
    hoveredStyle: SpanStyle? = null,
    pressedStyle: SpanStyle? = null
) = buildPatternAnnotation(
    pattern = pattern,
    caseSensitive = caseSensitive,
    spanStyle = spanStyle,
    linkAnnotationPlan = LinkAnnotationPlan(
        urlTagHandler = url,
        focusedStyle = focusedStyle,
        hoveredStyle = hoveredStyle,
        pressedStyle = pressedStyle
    )
)

fun linkPatternAnnotation(
    pattern: String,
    caseSensitive: Boolean = false,
    spanStyle: SpanStyle? = SpanStyle(textDecoration = TextDecoration.Underline),
    url: String,
    focusedStyle: SpanStyle? = null,
    hoveredStyle: SpanStyle? = null,
    pressedStyle: SpanStyle? = null
) = buildPatternAnnotation(
    pattern = pattern,
    caseSensitive = caseSensitive,
    spanStyle = spanStyle,
    linkAnnotationPlan = LinkAnnotationPlan(
        urlTagHandler = { url },
        focusedStyle = focusedStyle,
        hoveredStyle = hoveredStyle,
        pressedStyle = pressedStyle
    )
)

fun clickablePatternAnnotation(
    pattern: String,
    caseSensitive: Boolean = false,
    spanStyle: SpanStyle? = SpanStyle(textDecoration = TextDecoration.Underline),
    onClick: (matchingText: String) -> Unit,
    focusedStyle: SpanStyle? = null,
    hoveredStyle: SpanStyle? = null,
    pressedStyle: SpanStyle? = null
) = buildPatternAnnotation(
    pattern = pattern,
    caseSensitive = caseSensitive,
    spanStyle = spanStyle,
    linkAnnotationPlan = LinkAnnotationPlan(
        urlTagHandler = { it },
        onClick = onClick,
        focusedStyle = focusedStyle,
        hoveredStyle = hoveredStyle,
        pressedStyle = pressedStyle
    )
)