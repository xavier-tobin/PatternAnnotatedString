package com.xaviertobin.patternannotatedstring

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.xaviertobin.patternannotatedstring.compose.demo.Pill
import com.xaviertobin.patternannotatedstring.compose.demo.usernameToNameMap

import org.junit.Test

import org.junit.Assert.*


fun assertAnnotationsMatch(
    expected: AnnotatedString,
    actual: AnnotatedString,
) {
    assertTrue(expected.hasEqualAnnotations(actual))
}


class AnnotatedStringTests {

    @Test
    fun emptyPatternAnnotationsShouldProducePlainText() {
        val emptyAnnotations = emptyList<PatternAnnotation>()

        val result = emptyAnnotations.calculatePatternAnnotatedString("Hello, world!")

        assertAnnotationsMatch(
            AnnotatedString("Hello, world!"), result.annotatedString
        )
        assertEquals(true, result.styled)
        assertEquals(
            emptyList<ParagraphBackgroundAnnotation>(), result.paragraphBackgroundAnnotations
        )
        assertEquals(emptyMap<String, InlineTextContent>(), result.inlineContentMap)
        assertEquals(emptyList<DiscoveredInlineContent>(), result.discoveredInlineContent)
    }

    @Test
    fun basicPatternAnnotationShouldProduceStyledText() {

        val actual = listOf(
            TestFixtures.italicsMarkdown,
            TestFixtures.boldMarkdown
        ).calculatePatternAnnotatedString("I _love_ italics, but I **hate** bold")

        val expected = buildAnnotatedString {
            append("I ")
            withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                append("_love_")
            }
            append(" italics")
            append(", but I ")
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("**hate**")
            }
            append(" bold")
        }

        assertAnnotationsMatch(
            expected, actual.annotatedString
        )
    }

    @Test
    fun paragraphPatternAnnotationShouldProduceStyledText() {

        val actual =
            listOf(TestFixtures.rightAlignedAnnotation).calculatePatternAnnotatedString("I am left aligned\n->And I am right aligned").annotatedString

        val expected = buildAnnotatedString {
            append("I am left aligned\n")
            withStyle(ParagraphStyle(textAlign = TextAlign.End)) {
                append("(And I am right aligned)")
            }
        }

        assertAnnotationsMatch(
            expected, actual
        )
    }

    @Test
    fun inlineContentPatternAnnotationShouldProduceStyledText() {

        val actual =
            listOf(TestFixtures.usernameAnnotation).calculatePatternAnnotatedString("Shout-out to @user !")

        val expected = buildAnnotatedString {
            append("Shout-out to ")
            appendInlineContent("@user", "@user")
            append("!")
        }

        assertAnnotationsMatch(
            expected, actual.annotatedString
        )
    }

    @Test
    fun paragraphBackgroundAnnotationShouldProduceStyledText() {

        val actual =
            listOf(TestFixtures.codeBlockAnnotation).calculatePatternAnnotatedString("Plain text. ```code()``` More plain text.").annotatedString

        val expected = buildAnnotatedString {

            append("Plain text. ")

            withStyle(
                SpanStyle(
                    fontFamily = FontFamily.Monospace,
                )
            ) {
                withStyle(
                    ParagraphStyle(
                        lineHeight = 2.em, textIndent = TextIndent(
                            firstLine = 10.sp, restLine = 10.sp
                        )
                    )
                ) {
                    append("```code()```")
                }
            }

            append(" More plain text.")
        }

        assertAnnotationsMatch(
            expected, actual
        )
    }

    @Test
    fun multiplePatternAnnotationsShouldProduceStyledText() {

        val actual = listOf(
            TestFixtures.italicsMarkdown,
            TestFixtures.boldMarkdown,
            TestFixtures.rightAlignedAnnotation,
            TestFixtures.usernameAnnotation,
            TestFixtures.codeBlockAnnotation
        ).calculatePatternAnnotatedString(
            "I _love_ italics, but I **hate** bold" + "->I am right aligned\n" + "Shout-out to @user !" + "Plain text. ```code()``` More plain text."
        ).annotatedString

        val expected = buildAnnotatedString {

            append("I ")
            withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                append("_love_")
            }
            append(" italics")
            append(", but I ")
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("**hate**")
            }
            append(" bold")
            withStyle(ParagraphStyle(textAlign = TextAlign.End)) {
                append("->I am right aligned")
            }
            append("\nShout-out to ")
            appendInlineContent("@user", "@user")
            append(" !")
            append("Plain text. ")
            withStyle(
                SpanStyle(
                    fontFamily = FontFamily.Monospace,
                )
            ) {
                withStyle(
                    ParagraphStyle(
                        lineHeight = 2.em, textIndent = TextIndent(
                            firstLine = 10.sp, restLine = 10.sp
                        )
                    )
                ) {
                    append("```code()```")
                }
            }
            append(" More plain text.")
        }

        assertEquals(expected.toString(), actual.toString())

        assertAnnotationsMatch(
            expected, actual
        )
    }

    @Test
    fun linkAnnotationPlanShouldProduceStyledText() {

        val actual = listOf(
            TestFixtures.autoLinkAnnotation
        ).calculatePatternAnnotatedString("Check out https://bundlednotes.com").annotatedString

        val expected = buildAnnotatedString {
            append("Check out ")
            withLink(
                LinkAnnotation.Url(
                    "https://bundlednotes.com", styles = TextLinkStyles(
                        style = SpanStyle(
                            textDecoration = TextDecoration.Underline
                        ),
                    )
                )
            ) {
                append("https://bundlednotes.com")
            }
        }

        assertAnnotationsMatch(
            expected, actual
        )
    }


}