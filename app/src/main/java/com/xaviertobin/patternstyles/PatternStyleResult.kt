package com.xaviertobin.patternstyles

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Describes a plan to build an AnnotatedString, constructed by running the PatternAnnotations on a given text.
 */
private data class PatternAnnotationResult(
    val spanStyleAnnotations: List<AnnotatedString.Range<SpanStyle>> = listOf(),
    val paragraphStyleAnnotations: List<AnnotatedString.Range<ParagraphStyle>> = listOf(),
    val otherAnnotations: List<AnnotatedString.Range<out Any>> = listOf(),
    val paragraphBackgroundAnnotations: List<ParagraphBackgroundAnnotation> = listOf(),
    val discoveredInlineContent: List<DiscoveredInlineContent> = listOf()
)

data class ParagraphBackgroundAnnotation(val start: Int, val end: Int, val tag: String)

data class DiscoveredInlineContent(val contentId: String, val patternTag: String)


/**
 * Uses a list of pattern annotations to generate an AnnotationSpan
 */
private fun List<PatternStyle>.getPatternAnnotationResult(text: String): PatternAnnotationResult {

    val spanStyleAnnotations = mutableListOf<AnnotatedString.Range<SpanStyle>>()
    val paragraphStyleAnnotations = mutableListOf<AnnotatedString.Range<ParagraphStyle>>()
    val otherAnnotations = mutableListOf<AnnotatedString.Range<out Any>>()
    val backgrounds = mutableListOf<ParagraphBackgroundAnnotation>()
    val discoveredInlineContent = mutableListOf<DiscoveredInlineContent>()

    this.forEach {
        val matcher = it.pattern.matcher(text)
        while (matcher.find()) {

            val start = matcher.start()
            val end = matcher.end()
            val matchedString = text.substring(start, end)
            val matchCount = matcher.groupCount()
            val matchDetails = MatchDetails(start, end, matcher.group(matchCount))

            it.spanStyle?.invoke(matchDetails)?.let { style ->
                spanStyleAnnotations.add(AnnotatedString.Range(style, start, end))
            }

            it.paragraphStyle?.invoke(matchDetails)?.let { style ->
                paragraphStyleAnnotations.add(AnnotatedString.Range(style, start, end))
            }

            if (it.drawParagraphBackground != null) {
                backgrounds.add(
                    ParagraphBackgroundAnnotation(
                        start,
                        end - 1,
                        it.paragraphBackgroundTag
                    )
                )
            }

            if (it.inlineContentTag != null) {
                otherAnnotations.add(
                    AnnotatedString.Range(
                        item = it.inlineContentTag,
                        start = start,
                        end = end,
                        // Internal string in Compose INLINE_CONTENT_TAG, it's private for some reason
                        tag = "androidx.compose.foundation.text.inlineContent"
                    )
                )
                discoveredInlineContent.add(
                    DiscoveredInlineContent(
                        contentId = matchedString,
                        patternTag = it.inlineContentTag
                    )
                )
            }
        }
    }

    return PatternAnnotationResult(
        spanStyleAnnotations = spanStyleAnnotations,
        paragraphStyleAnnotations = paragraphStyleAnnotations,
        otherAnnotations = otherAnnotations,
        paragraphBackgroundAnnotations = backgrounds,
        discoveredInlineContent = discoveredInlineContent
    )
}







data class RichPatternAnnotatedString(
    val annotatedString: AnnotatedString,
    val paragraphBackgroundAnnotations: List<ParagraphBackgroundAnnotation> = listOf(),
    val discoveredInlineContent: List<DiscoveredInlineContent> = listOf()
)


/**
 * Remembers and returns an AnnotationPlan which can be used to build an AnnotatedString and apply styling.
 * Also returns detected inline content to render custom views
 */
@Composable
fun rememberRichPatternAnnotatedString(
    text: String,
    patternStyles: List<PatternStyle>
): RichPatternAnnotatedString {
    var richPatternAnnotatedString by remember { mutableStateOf(RichPatternAnnotatedString(AnnotatedString(text))) }
    val patternAnnotationResult = rememberPatternStyledResult(text, patternStyles)
    LaunchedEffect(patternAnnotationResult.first, patternAnnotationResult.second) {
        richPatternAnnotatedString = RichPatternAnnotatedString(
            annotatedString = AnnotatedString(
                patternAnnotationResult.first,
                patternAnnotationResult.second.spanStyleAnnotations,
                patternAnnotationResult.second.paragraphStyleAnnotations,
                patternAnnotationResult.second.otherAnnotations
            ),
            paragraphBackgroundAnnotations = patternAnnotationResult.second.paragraphBackgroundAnnotations,
            discoveredInlineContent = patternAnnotationResult.second.discoveredInlineContent
        )
    }
    return richPatternAnnotatedString
}

@Composable
fun rememberPatternAnnotatedString(
    text: String,
    patternStyles: List<PatternStyle>
): AnnotatedString {
    return rememberRichPatternAnnotatedString(text, patternStyles).annotatedString
}

/**
 * rememberPatternStyledResult doesn't ever return text, this is important to allow text and style to be "out of sync"
 */
@Composable
private fun rememberPatternStyledResult(
    text: String,
    patternStyles: List<PatternStyle>
): Pair<String, PatternAnnotationResult> {

    var patternAnnotationResult by remember { mutableStateOf(text to PatternAnnotationResult()) }

    LaunchedEffect(text, patternStyles) {
        withContext(Dispatchers.Default) {
            patternAnnotationResult = text to patternStyles.getPatternAnnotationResult(text)
        }
    }

    return patternAnnotationResult
}
