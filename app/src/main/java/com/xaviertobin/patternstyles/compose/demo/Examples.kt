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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.xaviertobin.patternstyles.basicPatternAnnotation
import com.xaviertobin.patternstyles.annotatedWith
import com.xaviertobin.patternstyles.clickablePatternAnnotation
import com.xaviertobin.patternstyles.drawParagraphBackgrounds
import com.xaviertobin.patternstyles.patternAnnotatedString
import com.xaviertobin.patternstyles.inlineTextContent
import com.xaviertobin.patternstyles.inlineContentPatternAnnotation
import com.xaviertobin.patternstyles.linkPatternAnnotation
import com.xaviertobin.patternstyles.paragraphPatternAnnotation
import com.xaviertobin.patternstyles.rememberParagraphBackgrounds


/**
 * # Basic italics example
 */

val italicsMarkdown = basicPatternAnnotation(
    pattern = "_.*?_",
    spanStyle = SpanStyle(fontStyle = FontStyle.Italic),
)

@Preview
@Composable
fun BasicItalicsExample() {
    PreviewLayout {
        Text(
            // annotatedWith accepts either one or a list of PatternAnnotations
            // So you can apply multiple styles to the same text
            text = "I _love_ italic text!".annotatedWith(italicsMarkdown)
        )
    }
}

/**
 * # Basic styling
 */

val redFruit = basicPatternAnnotation(
    pattern = "(\\w*berry)|(\\w{0,}apple)",
    spanStyle = SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold)
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
 * # Links & hyperlinks
 */
val linkAnnotation = linkPatternAnnotation(
    pattern = "Bundled Notes",
    url = "https://bundlednotes.com"
)

@Preview
@Composable
fun BasicLinkExample() {
    PreviewLayout {
        Text(
            text = "Check out the Bundled Notes website!".annotatedWith(linkAnnotation)
        )
    }
}


@Preview
@Composable
fun BasicClickExample() {

    var clickCount by remember { mutableIntStateOf(0) }

    val clickableAnnotation = remember {
        clickablePatternAnnotation(
            pattern = "banana",
            onClick = { clickCount++ }
        )
    }

    PreviewLayout {
        Text(
            text = "The word banana has been clicked $clickCount times".annotatedWith(clickableAnnotation)
        )
    }
}


/**
 * ## Search text highlighting
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
            literalPattern = true,
            spanStyle = SpanStyle(
                background = Color.Yellow,
            ),
        )
    }

    val highlightedText = TEXT_TO_SEARCH.annotatedWith(
        patternAnnotation = highlightAnnotation
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
 */


val rightAlignedAnnotation = paragraphPatternAnnotation(
    pattern = "\\(.+\\)",
    paragraphStyle = ParagraphStyle(
        textAlign = TextAlign.End
    )
)

@Preview
@Composable
fun ParagraphAlignmentExample() {

    val annotated = "I am left aligned\n(And I am right aligned)".patternAnnotatedString(
        patternAnnotation = rightAlignedAnnotation
    )

    PreviewLayout {
        Text(
            text = annotated.annotatedString,
            modifier = Modifier.fillMaxWidth(),
        )
    }

}


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

    val annotated = multiParagraphText.patternAnnotatedString(
        patternAnnotation = codeBlockAnnotation
    )

    val backgroundsResult = rememberParagraphBackgrounds(
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
 * ## Inline content
 */

val usernameAnnotation = inlineContentPatternAnnotation(
    pattern = "@[A-Za-z0-9_]+",
    inlineContent = { matchedText ->
        inlineTextContent(width = 6.8.em, height = 1.4.em) {
            // You can use any composable here, but stick within the above bounds^
            Pill(usernameToNameMap[matchedText] ?: matchedText)
        }
    }
)

val usernameToNameMap = mapOf(
    "@xavier" to "Xavier Tobin",
)

@Preview
@Composable
fun SimpleInlineExample() {

    val styledComment = "Thanks @xavier, this is cool!".patternAnnotatedString(
        patternAnnotation = usernameAnnotation
    )

    PreviewLayout {
        Text(
            text = styledComment.annotatedString,
            inlineContent = styledComment.inlineContentMap
        )
    }
}

@Composable
fun Pill(text: String) {
    Text(
        text,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(0.dp)
            .background(
                color = Color.Green.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 8.dp, vertical = 1.dp)
            .fillMaxWidth()
    )
}


/**
 * ## Combined examples
 */

val italics = basicPatternAnnotation(
    pattern = "_.*?_",
    spanStyle = SpanStyle(fontStyle = FontStyle.Italic)
)

@Preview
@Composable
fun CombinedExample() {

    val userComment = "Thanks @xavier, this is cool! " +
            "I also _love_ italics - and I'll be sure to check out Bundled Notes. " +
            "Coincidentally, I had an Apple and a strawberry today." +
            "(I'll star this repository!)"

    val styledComment = userComment.patternAnnotatedString(
        patternAnnotations = listOf(
            usernameAnnotation,
            italics,
            redFruit,
            linkAnnotation,
            rightAlignedAnnotation
        )
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