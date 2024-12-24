package com.xaviertobin.patternannotatedstring

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.text.TextLayoutResult


data class ParagraphBackgroundsResult(
    val backgroundsToDraw: List<BackgroundsToDraw>,
    val onTextLayout: (textLayoutResult: (TextLayoutResult)) -> Unit
)

data class BackgroundsToDraw(
    val rect: Rect,
    val onDraw: OnDrawBackground
)

@Composable
fun rememberParagraphBackgrounds(
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


fun Modifier.drawParagraphBackgrounds(backgroundsToDraw: List<BackgroundsToDraw>) = this.drawBehind {
    backgroundsToDraw.forEach { backgroundToDraw ->
        backgroundToDraw.onDraw(this, backgroundToDraw.rect)
    }
}


fun calculateParagraphBackgrounds(
    paragraphBackgroundAnnotations: List<ParagraphBackgroundAnnotation>,
    textLayoutResult: TextLayoutResult
): List<BackgroundsToDraw> {
    return paragraphBackgroundAnnotations.map { (startIndex, endIndex, onDraw) ->
        BackgroundsToDraw(
            rect = getParagraphBounds(
                textLayoutResult = textLayoutResult,
                startLine = textLayoutResult.getLineForOffset(startIndex),
                endLine = textLayoutResult.getLineForOffset(endIndex),
            ),
            onDraw = onDraw
        )
    }
}

fun getParagraphBounds(
    textLayoutResult: TextLayoutResult,
    startLine: Int,
    endLine: Int,
): Rect {
    return Rect(
        left = 0f,
        top = textLayoutResult.getLineTop(startLine),
        right =  textLayoutResult.getLineRight(endLine),
        bottom = textLayoutResult.getLineBottom(endLine)
    )
}


