package com.xaviertobin.patternstyles

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.StringAnnotation
import androidx.compose.ui.text.TextLinkStyles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class PatternAnnotatedString(
    val annotatedString: AnnotatedString,
    val styled: Boolean,
    val paragraphBackgroundAnnotations: List<ParagraphBackgroundAnnotation> = listOf(),
    val inlineContentMap: Map<String, InlineTextContent> = mapOf(),
    val discoveredInlineContent: List<DiscoveredInlineContent> = listOf(),
)

data class ParagraphBackgroundAnnotation(
    val start: Int,
    val end: Int,
    val onDraw: OnDrawBackground
)

data class DiscoveredInlineContent(
    val contentId: String,
    val patternTag: String
)

enum class PerformanceStrategy {
    Performant,
    Immediate,
}

/**
 * Uses a list of pattern annotations to generate an AnnotationSpan
 */
internal fun List<PatternAnnotation>.calculatePatternAnnotatedString(text: String): PatternAnnotatedString {
    val allAnnotations = mutableListOf<AnnotatedString.Range<out AnnotatedString.Annotation>>()
    val backgrounds = mutableListOf<ParagraphBackgroundAnnotation>()
    val discoveredInlineContent = mutableListOf<DiscoveredInlineContent>()
    val inlineContentMap = mutableMapOf<String, InlineTextContent>()

    this.forEach { patternAnnotation ->
        val matcher = patternAnnotation.pattern.matcher(text)
        while (matcher.find()) {

            val start = matcher.start()
            val end = matcher.end()
            val matchedString = text.substring(start, end)
            val matchCount = matcher.groupCount()
            val matchDetails = MatchDetails(start, end, matcher.group(matchCount))

            if (patternAnnotation.linkAnnotationPlan != null) {

                val tagOrUrl = patternAnnotation.linkAnnotationPlan.urlTagHandler(matchedString)
                val textLinkStyles = TextLinkStyles(
                    style = patternAnnotation.spanStyle?.invoke(matchDetails),
                    focusedStyle = patternAnnotation.linkAnnotationPlan.focusedStyle,
                    hoveredStyle = patternAnnotation.linkAnnotationPlan.hoveredStyle,
                    pressedStyle = patternAnnotation.linkAnnotationPlan.pressedStyle
                )

                allAnnotations.add(
                    AnnotatedString.Range(
                        item = if (patternAnnotation.linkAnnotationPlan.onClick != null) LinkAnnotation.Clickable(
                            tag = tagOrUrl,
                            styles = textLinkStyles,
                            linkInteractionListener = {
                                patternAnnotation.linkAnnotationPlan.onClick.invoke(
                                    tagOrUrl
                                )
                            }
                        ) else LinkAnnotation.Url(
                            url = tagOrUrl,
                            styles = textLinkStyles
                        ),
                        start = start,
                        end = end
                    )
                )
            } else {

                patternAnnotation.spanStyle?.invoke(matchDetails)?.let { style ->
                    allAnnotations.add(AnnotatedString.Range(style, start, end))
                }

                patternAnnotation.paragraphStyle?.invoke(matchDetails)?.let { style ->
                    allAnnotations.add(AnnotatedString.Range(style, start, end))
                }

                patternAnnotation.drawParagraphBackground?.let { drawParagraphBackground ->
                    backgrounds.add(
                        ParagraphBackgroundAnnotation(
                            start,
                            end - 1,
                            drawParagraphBackground
                        )
                    )
                }

                patternAnnotation.inlineContentTag?.let { tag ->
                    allAnnotations.add(
                        AnnotatedString.Range(
                            item = StringAnnotation(tag),
                            start = start,
                            end = end,
                            // Internal string in Compose INLINE_CONTENT_TAG, it's private for some reason
                            tag = "androidx.compose.foundation.text.inlineContent"
                        )
                    )
                    discoveredInlineContent.add(
                        DiscoveredInlineContent(
                            contentId = matchedString,
                            patternTag = tag
                        )
                    )
                }

                patternAnnotation.inlineContent?.let { inlineContent ->
                    inlineContentMap[matchedString] = inlineContent(matchedString)
                    allAnnotations.add(
                        AnnotatedString.Range(
                            item = StringAnnotation(matchedString),
                            start = start,
                            end = end,
                            // Internal string in Compose INLINE_CONTENT_TAG, it's private for some reason
                            tag = "androidx.compose.foundation.text.inlineContent"
                        )
                    )
                }
            }
        }
    }

    return PatternAnnotatedString(
        annotatedString = AnnotatedString(text, allAnnotations),
        styled = true,
        paragraphBackgroundAnnotations = backgrounds,
        discoveredInlineContent = discoveredInlineContent,
        inlineContentMap = inlineContentMap
    )
}


/**
 * Remembers and returns an AnnotationPlan which can be used to build an AnnotatedString and apply styling.
 * Also returns detected inline content to render custom views
 */
@Composable
fun String.patternAnnotatedString(
    patternAnnotations: List<PatternAnnotation>,
    performanceStrategy: PerformanceStrategy = PerformanceStrategy.Immediate
): PatternAnnotatedString {

    val isImmediate = performanceStrategy == PerformanceStrategy.Immediate

    var patternAnnotatedString by remember(
        key1 = if (isImmediate) this else Unit,
        key2 = if (isImmediate) patternAnnotations else Unit,
    ) {
        mutableStateOf(
            when (performanceStrategy) {
                PerformanceStrategy.Immediate -> patternAnnotations
                    .calculatePatternAnnotatedString(this)

                PerformanceStrategy.Performant -> PatternAnnotatedString(
                    annotatedString = AnnotatedString(this),
                    styled = false
                )
            }
        )

    }

    LaunchedEffect(
        key1 = this,
        key2 = patternAnnotations
    ) {
        if (!isImmediate) {
            launch(Dispatchers.Default) {
                patternAnnotatedString = patternAnnotations
                    .calculatePatternAnnotatedString(this@patternAnnotatedString)
            }
        }
    }

    return patternAnnotatedString
}

@Composable
fun String.patternAnnotatedString(
    patternAnnotation: PatternAnnotation,
    performanceStrategy: PerformanceStrategy = PerformanceStrategy.Immediate
): PatternAnnotatedString {
    return this.patternAnnotatedString(
        patternAnnotations = listOf(patternAnnotation),
        performanceStrategy = performanceStrategy
    )
}

@Composable
fun String.annotatedWith(
    patternAnnotations: List<PatternAnnotation>,
    performanceStrategy: PerformanceStrategy = PerformanceStrategy.Immediate
): AnnotatedString {
    return this.patternAnnotatedString(
        patternAnnotations = patternAnnotations,
        performanceStrategy = performanceStrategy
    ).annotatedString
}

@Composable
fun String.annotatedWith(
    patternAnnotation: PatternAnnotation,
    performanceStrategy: PerformanceStrategy = PerformanceStrategy.Immediate
): AnnotatedString {
    return this.patternAnnotatedString(
        patternAnnotations = listOf(patternAnnotation),
        performanceStrategy = performanceStrategy
    ).annotatedString
}
