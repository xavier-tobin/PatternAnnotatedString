package com.xaviertobin.patternstyles

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class PatternAnnotatedString(
    val annotatedString: AnnotatedString,
    val styled: Boolean,
    val paragraphBackgroundAnnotations: List<ParagraphBackgroundAnnotation> = listOf(),
    val discoveredInlineContent: List<DiscoveredInlineContent> = listOf(),
    val inlineContentMap: Map<String, InlineTextContent> = mapOf(),
)

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
