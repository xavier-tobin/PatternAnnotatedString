package com.xaviertobin.patternstyles

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.StringAnnotation


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

    this.forEach {
        val matcher = it.pattern.matcher(text)
        while (matcher.find()) {

            val start = matcher.start()
            val end = matcher.end()
            val matchedString = text.substring(start, end)
            val matchCount = matcher.groupCount()
            val matchDetails = MatchDetails(start, end, matcher.group(matchCount))

            it.spanStyle?.invoke(matchDetails)?.let { style ->
                allAnnotations.add(AnnotatedString.Range(style, start, end))
            }

            it.paragraphStyle?.invoke(matchDetails)?.let { style ->
                allAnnotations.add(AnnotatedString.Range(style, start, end))
            }

            it.drawParagraphBackground?.let { drawParagraphBackground ->
                backgrounds.add(
                    ParagraphBackgroundAnnotation(
                        start,
                        end - 1,
                        drawParagraphBackground
                    )
                )
            }

            it.inlineContentTag?.let { tag ->
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

            it.inlineContent?.let { inlineContent ->
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

    return PatternAnnotatedString(
        annotatedString = AnnotatedString(text, allAnnotations),
        styled = true,
        paragraphBackgroundAnnotations = backgrounds,
        discoveredInlineContent = discoveredInlineContent,
        inlineContentMap = inlineContentMap
    )
}


