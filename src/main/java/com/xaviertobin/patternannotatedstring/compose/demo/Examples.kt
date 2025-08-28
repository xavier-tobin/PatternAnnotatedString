package com.xaviertobin.patternannotatedstring.compose.demo

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xaviertobin.patternannotatedstring.PerformanceStrategy
import com.xaviertobin.patternannotatedstring.annotatedWith
import com.xaviertobin.patternannotatedstring.drawParagraphBackgrounds
import com.xaviertobin.patternannotatedstring.rememberBasicPatternAnnotation
import com.xaviertobin.patternannotatedstring.rememberClickablePatternAnnotation
import com.xaviertobin.patternannotatedstring.rememberParagraphBackgrounds
import com.xaviertobin.patternannotatedstring.richAnnotatedWith


/**
 * # Basic italics example
 */

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
 * # Links, clickable and hyperlinks
 */

@Preview
@Composable
fun BasicHyperlink() {
    PreviewLayout {
        Text(
            text = "Check out the Bundled Notes website!".annotatedWith(linkAnnotation)
        )
    }
}

@Preview
@Composable
fun AutoLinks() {
    PreviewLayout {
        Text(
            text = "Check out https://bundlednotes.com on the Play Store: https://play.google.com/store/apps/details?id=com.xaviertobin.noted".annotatedWith(autoLinkAnnotation)
        )
    }
}

@Preview
@Composable
fun MailToLinks() {
    PreviewLayout {
        Text(
            text = "You can email support here: support@bundlednotes.com".annotatedWith(
                emailMailToLinkAnnotation
            )
        )
    }
}

@Preview
@Composable
fun BasicClickExample() {
    var clickCount by remember { mutableIntStateOf(0) }

    val clickableAnnotation = rememberClickablePatternAnnotation(
        pattern = "banana",
        onClick = {
            clickCount++
        }
    )

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

    val highlightAnnotation = rememberBasicPatternAnnotation(
        pattern = searchQuery,
        literalPattern = true,
        spanStyle = SpanStyle(
            background = Color.Yellow,
        )
    )

    val highlightedText = TEXT_TO_SEARCH.annotatedWith(
        patternAnnotation = highlightAnnotation,
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
 */

@Preview
@Composable
fun ParagraphAlignmentExample() {

    val annotated = "I am left aligned\n(And I am right aligned)".richAnnotatedWith(
        patternAnnotation = rightAlignedAnnotation
    )

    PreviewLayout {
        Text(
            text = annotated.annotatedString,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
fun ParagraphStyling() {

    val annotated = "```\ncode();\n```\n\nNormal text".richAnnotatedWith(
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

val usernameToNameMap = mapOf(
    "@xavier" to "Xavier Tobin",
)

@Preview
@Composable
fun SimpleInlineExample() {

    val styledComment = "Thanks @xavier, this is cool!".richAnnotatedWith(
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

@Preview
@Composable
fun CombinedExample() {

    val userComment = "Thanks @xavier, this is cool! " +
            "I also _love_ italics - and I'll be sure to check out Bundled Notes. " +
            "Coincidentally, I had an Apple and a strawberry today." +
            "(I'll star this repository!)"

    val styledComment = userComment.richAnnotatedWith(
        patternAnnotations = listOf(
            usernameAnnotation,
            italicsMarkdown,
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

