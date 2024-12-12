package com.xaviertobin.patternstyles

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextLayoutResult


data class ParagraphBackgroundsResult(
    val backgroundsToDraw: List<BackgroundsToDraw>,
    val onTextLayout: (textLayoutResult: (TextLayoutResult)) -> Unit
)

data class BackgroundsToDraw(
    val rect: Rect,
    val tag: String
)

@Composable
fun useParagraphBackgrounds(
    paragraphBackgroundAnnotations: List<ParagraphBackgroundAnnotation>,
): ParagraphBackgroundsResult {

    var backgroundsToDraw by remember { mutableStateOf(listOf<BackgroundsToDraw>()) }

    return ParagraphBackgroundsResult(
        backgroundsToDraw = backgroundsToDraw,
        onTextLayout = { textLayoutResult ->
            backgroundsToDraw = calculateParagraphBackgrounds(
                paragraphBackgroundAnnotations = paragraphBackgroundAnnotations,
                textLayoutResult = textLayoutResult
            )
        }
    )
}


fun Modifier.drawParagraphBackgrounds(backgroundsToDraw: List<BackgroundsToDraw>, onDraw: DrawScope.(BackgroundsToDraw) -> Unit) = this.drawBehind {
    backgroundsToDraw.forEach { backgroundToDraw ->
        onDraw(backgroundToDraw)
    }
}


fun calculateParagraphBackgrounds(
    paragraphBackgroundAnnotations: List<ParagraphBackgroundAnnotation>,
    textLayoutResult: TextLayoutResult
): List<BackgroundsToDraw> {
    return paragraphBackgroundAnnotations.map { (startIndex, endIndex, tag) ->
        BackgroundsToDraw(
            rect = getParagraphBounds(
                textLayoutResult = textLayoutResult,
                startLine = textLayoutResult.getLineForOffset(startIndex),
                endLine = textLayoutResult.getLineForOffset(endIndex),
            ),
            tag = tag
        )
    }
}

fun getParagraphBounds(
    textLayoutResult: TextLayoutResult,
    startLine: Int,
    endLine: Int,
): Rect {

    val startOffsetX = textLayoutResult.getLineLeft(startLine)
    val startOffsetY = textLayoutResult.getLineTop(startLine)
    val endOffsetX = textLayoutResult.getLineRight(endLine)
    val endOffsetY = textLayoutResult.getLineBottom(endLine)

    return Rect(
        left = startOffsetX,
        top = startOffsetY,
        right = endOffsetX,
        bottom = endOffsetY
    )
}


