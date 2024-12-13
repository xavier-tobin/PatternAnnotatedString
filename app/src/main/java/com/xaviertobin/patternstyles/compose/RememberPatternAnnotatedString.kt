package com.xaviertobin.patternstyles.compose

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import com.xaviertobin.patternstyles.DiscoveredInlineContent
import com.xaviertobin.patternstyles.ParagraphBackgroundAnnotation
import com.xaviertobin.patternstyles.PatternAnnotation
import com.xaviertobin.patternstyles.PatternAnnotationResult
import com.xaviertobin.patternstyles.getPatternAnnotationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


data class RichPatternAnnotatedString(
    val annotatedString: AnnotatedString,
    val paragraphBackgroundAnnotations: List<ParagraphBackgroundAnnotation> = listOf(),
    val discoveredInlineContent: List<DiscoveredInlineContent> = listOf(),
    val inlineContentMap: Map<String, InlineTextContent> = mapOf()
)


/**
 * Remembers and returns an AnnotationPlan which can be used to build an AnnotatedString and apply styling.
 * Also returns detected inline content to render custom views
 */
@Composable
fun String.richAnnotated(
    patternAnnotations: List<PatternAnnotation>
): RichPatternAnnotatedString {
    
    var richPatternAnnotatedString by remember {
        mutableStateOf(
            RichPatternAnnotatedString(
                AnnotatedString(this)
            )
        )
    }

    val patternAnnotationResult = rememberPatternStyledResult(this, patternAnnotations)

    LaunchedEffect(patternAnnotationResult.first, patternAnnotationResult.second) {
        richPatternAnnotatedString = RichPatternAnnotatedString(
            annotatedString = AnnotatedString(
                patternAnnotationResult.first,
                patternAnnotationResult.second.rangedAnnotations,
            ),
            paragraphBackgroundAnnotations = patternAnnotationResult.second.paragraphBackgroundAnnotations,
            discoveredInlineContent = patternAnnotationResult.second.discoveredInlineContent,
            inlineContentMap = patternAnnotationResult.second.inlineContentMap
        )
    }
    return richPatternAnnotatedString
}

@Composable
fun String.annotated(
    patternAnnotations: List<PatternAnnotation>
): AnnotatedString {
    return this.richAnnotated(patternAnnotations).annotatedString
}

/**
 * rememberPatternStyledResult doesn't ever return text, this is important to allow text and style to be "out of sync"
 */
@Composable
private fun rememberPatternStyledResult(
    text: String,
    patternAnnotations: List<PatternAnnotation>
): Pair<String, PatternAnnotationResult> {

    var patternAnnotationResult by remember { mutableStateOf(text to PatternAnnotationResult()) }

    LaunchedEffect(text, patternAnnotations) {
        withContext(Dispatchers.Default) {
            patternAnnotationResult = text to patternAnnotations.getPatternAnnotationResult(text)
        }
    }

    return patternAnnotationResult
}
