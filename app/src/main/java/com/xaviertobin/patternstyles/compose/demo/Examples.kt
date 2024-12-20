package com.xaviertobin.patternstyles.compose.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.xaviertobin.patternstyles.PerformanceStrategy
import com.xaviertobin.patternstyles.basicPatternAnnotation
import com.xaviertobin.patternstyles.annotatedWith
import com.xaviertobin.patternstyles.drawParagraphBackgrounds
import com.xaviertobin.patternstyles.getPatternAnnotatedString
import com.xaviertobin.patternstyles.inlineContent
import com.xaviertobin.patternstyles.inlineContentPatternAnnotation
import com.xaviertobin.patternstyles.paragraphPatternAnnotation
import com.xaviertobin.patternstyles.useParagraphBackgrounds


/**
 * # Basic usage
 *
 * 1. Create a `PatternAnnotation` which maps a pattern to styles.
 * 2. Use `string.annotatedWith()` to create an `AnnotatedString` with the styles applied.
 */

val redFruit = basicPatternAnnotation(
    pattern = "(\\w*berry)|(\\w{0,}apple)",
    spanStyle = SpanStyle(color = Color.Red)
)

@Preview
@Composable
fun BasicExample() {
    PreviewLayout {
        Text(
            // annotatedWith accepts either one or a list of PatternAnnotations
            // So you can apply multiple styles to the same text
            text = "Strawberry Fridge Apple Ferrari".annotatedWith(redFruit)
        )
    }
}

/**
 * #### Note
 *
 * `annotatedWith` is a Composable function and only re-calculates styles if the text or annotations/s change.
 * Many styles, long text or complicated patterns may impact performance, but the library includes
 * options to cater for this - please see the Performance considerations section.
 */


/**
 * ## Search text highlighting (& other dynamic patterns)
 * 
 * There are many use-cases for styling whereby you may not know the pattern at compile-time.
 * For example, you may want to highlight matching text based on a search query.
 * This is easy to achieve, but there are some performance considerations.
 * 
 * 1. Create a `PatternAnnotation` with a dynamic pattern, wrapped in a `remember` block.
 * 2. Use `string.annotatedWith()` to apply the style/s to a string with the PerformanceStrategy.Performant option.
 * 
 */

const val TEXT_TO_SEARCH = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
        "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
        "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
        "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
        "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

@Preview
@Composable
fun SearchTextHighlighting() {

    var searchQuery by remember { mutableStateOf("reprehenderit") }

    // You have to create a pattern annotation using the search query
    // For performance reasons, you should remember the pattern annotation with the search query as a key
    val highlightAnnotation = remember(searchQuery) {
        basicPatternAnnotation(
            pattern = searchQuery,
            spanStyle = SpanStyle(
                background = Color.Yellow,
            )
        )
    }

    val highlightedText = TEXT_TO_SEARCH.annotatedWith(
        patternAnnotation = highlightAnnotation,
        // This means that annotations will be calculated in a background thread
        // The text will be un-styled until the annotations are calculated
        performanceStrategy = PerformanceStrategy.Performant
    )

    PreviewLayout {

        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it }
        )

        Text(
            text = highlightedText,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}


/**
 * ## Paragraph styling
 *
 * By default, AnnotatedString and Paragraph Style only supports changing text arrangement/layout properties.
 * This library adds the ability to draw backgrounds behind paragraphs.
 *
 * 1. Create a `PatternAnnotation` with Paragraph styles and/or backgrounds.
 * 2. Use `string.getPatternAnnotatedString()` to apply the style/s to a string.
 * 3. Use `useParagraphBackgrounds` if you want to draw paragraph backgrounds.
 * 4. Pass the annotatedString and `useParagraphBackgrounds` result to a `Text` composable.
 */

// This is a much more complex pattern annotation, but it's still pretty simple!
val codeBlockAnnotation = paragraphPatternAnnotation(
    pattern = "```[^` ][^`]*[^ ]?```",
    spanStyle = SpanStyle(
        fontFamily = FontFamily.Monospace,
    ),
    paragraphStyle = ParagraphStyle(
        // The code block looks better with these paragraph settings
        lineHeight = 2.em,
        lineHeightStyle = LineHeightStyle(
            LineHeightStyle.Alignment.Proportional,
            LineHeightStyle.Trim.LastLineBottom
        ),
        textIndent = TextIndent(
            firstLine = 10.sp,
            restLine = 10.sp
        ),
    ),
    onDrawParagraphBackground = { rect ->
        // You can draw pretty much anything here, but...
        // This runs on the main thread, so keep it simple
        val fullWidthRect = rect.copy(
            right = size.width
        )
        drawRoundRect(
            color = Color.LightGray,
            topLeft = fullWidthRect.topLeft,
            size = fullWidthRect.size,
            cornerRadius = CornerRadius(
                10.dp.toPx(), 10.dp.toPx()
            )
        )
    }
)

const val multiParagraphText = "```\ncode();\n```\n\nNormal text"

@Preview
@Composable
fun ParagraphStyling() {

    val annotated = multiParagraphText.getPatternAnnotatedString(
        patternAnnotation = codeBlockAnnotation
    )

    val backgroundsResult = useParagraphBackgrounds(
        paragraphBackgroundAnnotations = annotated.paragraphBackgroundAnnotations
    )

    PreviewLayout {
        // Note that backgrounds will only be drawn on a second render, because their positions are initially unknown
        Text(
            text = annotated.annotatedString,
            modifier = Modifier
                .fillMaxWidth()
                // This is where the backgrounds are drawn
                .drawParagraphBackgrounds(backgroundsResult.backgroundsToDraw),

            // This is where the backgrounds are calculated
            onTextLayout = backgroundsResult.onTextLayout,
        )
    }

}

/**
 * *Be careful!*
 *
 * There are a few things to keep in mind when styling paragraphs with this library:
 * 1. Overlapping paragraphs will cause a crash! Ensure that your patterns are mutually exclusive.
 * 2. Backgrounds are drawn on the main thread, so keep them simple.
 * 3. Backgrounds only appear on the second render, so keep this in mind when designing your UI.
 * 
 */


/**
 * ## Inline content
 *
 * `buildAnnotatedString` includes the ability to replace matching text with an inline composable, but it can be cumbersome to use.
 * To achieve this dynamically, `String.getPatternAnnotatedString()` can build and return an inline content map.
 *
 * 1. Create a `PatternAnnotation` with an `inlineContent` function.
 * 2. Use `string.getPatternAnnotatedString()` to apply the style/s to a string.
 * 3. Pass the resulting `annotatedString` and `inlineContentMap` to a `Text` composable.
 */

val usernameAnnotation = inlineContentPatternAnnotation(
    pattern = "@[A-Za-z0-9_]+",
    inlineContent = { matchedText ->
        inlineContent(width = 7.3.em, height = 1.8.em) {
            // You can use any composable here, but stick within the above bounds^
            Pill(usernameToNameMap[matchedText] ?: matchedText)
        }
    }
)

@Preview
@Composable
fun SimpleInlineExample() {

    val styledComment = "Thanks @xavier, this is cool!".getPatternAnnotatedString(
        patternAnnotation = usernameAnnotation
    )

    PreviewLayout {
        Text(
            text = styledComment.annotatedString,
            inlineContent = styledComment.inlineContentMap
        )
    }
}

/**
 * Note, as these use cases get more complex, you may want to be measure and consider performance.
 * Please
 */

// Extra code for the preview
val usernameToNameMap = mapOf(
    "@xavier" to "Xavier Tobin",
)

@Composable
fun Pill(text: String) {
    Text(
        text,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(2.dp)
            .background(
                color = Color.Green.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 10.dp, vertical = 2.dp)
            .fillMaxWidth()
    )
}


/**
 * ## Combined examples
 *
 * You can combine and customise all kinds of pattern annotations to create rich styled text.
 *
 * All you have to do is pass multiple pattern annotations to `annotatedWith` or `getPatternAnnotatedString`.
 */

val italics = basicPatternAnnotation(
    pattern = "_.*?_",
    spanStyle = SpanStyle(fontStyle = FontStyle.Italic)
)

@Preview
@Composable
fun CombinedExample() {

    val styledComment = "Thanks @xavier, this is _cool!_ I would like to give you an apple to say thanks :)".getPatternAnnotatedString(
        patternAnnotations = listOf(usernameAnnotation, italics, redFruit)
    )

    PreviewLayout {
        Text(
            text = styledComment.annotatedString,
            inlineContent = styledComment.inlineContentMap
        )
    }
}

@Composable
fun PreviewLayout(
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(0.dp))
            .width(width = 450.dp)
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 30.dp)

    ) {
        content()
    }

}



//val quoteBlockStyle = paragraphPatternAnnotation(
//    pattern = "(?<=^)(>.*\\n?)|(?<=\\n)(>.*\\n?)",
//    spanStyle = SpanStyle(fontSize = 16.sp),
//    paragraphStyle = ParagraphStyle(
//        lineHeight = 2.em,
//        lineBreak = LineBreak.Unspecified,
//        textIndent = TextIndent(
//            firstLine = 12.sp,
//            restLine = 12.sp
//        )
//    ),
//    drawParagraphBackground = { rect ->
//        drawRoundRect(
//            color = Color.LightGray,
//            topLeft = rect.topLeft,
//            size = Size(4.dp.toPx(), rect.height),
//            cornerRadius = CornerRadius(
//                10.dp.toPx(), 10.dp.toPx()
//            )
//        )
//    }
//)