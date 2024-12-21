# PatternAnnotatedString

Easily style text using patterns/regular expressions in Jetpack Compose.

- [x] 📝 Alternative to `buildAnnotatedString` for styling user-generated or dynamic text
- [x] 🎨 Simple, highly flexible API
- [x] 🚀 Respects Compose lifecycle with performance options
- [x] 📦 Out-of-the-box support for custom paragraph backgrounds
- [x] 🧩 Easily render custom inline content
- [x] 📚 Can be used to render markdown, or style text based on custom syntax

> _Built to render markdown previews and for highlighting global search results
in [Bundled Notes](https://bundlednotes.com)._

## Readiness checklist

`PatternAnnotatedString` has been an internal experiment for about a month, but I plan to release it as a
maintained library after I've done the following:

- [ ] Wait until stable compose-bom includes latest StringAnnotation APIs
- [ ] Add more examples to README
- [ ] Add tests
- [ ] Work out how to release library via Maven/Gradle

# Basic usage

The library has a very simple API and in most cases you can generate a styled `AnnotatedString` with
just a few lines of code. All you have to do is:

1. Create PatternAnnotation/s that map patterns to text styles:
    ```kotlin
    val italicsMarkdown = basicPatternAnnotation(
        pattern = "_.*?_",
        spanStyle = SpanStyle(fontStyle = FontStyle.Italic),
    )
    ```

2. Use `String.annotatedWith(patternAnnotation/s)` in a Composable to get an AnnotatedString:
    ```kotlin
    val annotatedString = "I love _italic_ text!".annotatedWith(italicsMarkdown)
    ```

3. Use the result in a Composable:
    ```kotlin
    Text(text = annotatedString)
    ```
   ![Basic italics usage example](images/basic_italics_example.png)

# Advanced usage

For basic text styling, `AnnotatedString` does the job. However, `PatternAnnotatedString` supports
some extra features that `AnnotatedString` does not, most notably paragraph backgrounds and dynamic
inline-content. To use these extra features, all you need to do is use `patternAnnotatedString()`
instead of `annotatedWith()`. Rather than returning a plain `AnnotatedString`, it returns a
`PatternAnnotatedString` with extra goodies to render paragraph backgrounds and inline content. See
the examples below for more information.

# Examples

### Basic text styling

> Italics, bold, font style, shadow, background etc.




```kotlin
val redFruit = basicPatternAnnotation(
    pattern = "(\\w*berry)|(\\w{0,}apple)",
    spanStyle = SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold)
)

@Composable
fun BasicExample() {
    Text(
        text = "Strawberry Fridge Apple Ferrari".annotatedWith(redFruit)
    )
}
```

###### Result:

![Basic Example](images/basic_example.png)


> [!IMPORTANT]
> `annotatedWith` is a Composable function and only re-calculates styles if the text or
> annotations/s change.
> Many annotations, long text or complex patterns may impact performance, but the library includes
> options to cater for this - please see the Performance considerations section.

### Dynamic text styles

> Search text highlighting, find in page etc.

You may want to style text based on a pattern you don't know at compile-time. For example,
highlighting matching text in results from a search query the user inputs. This is _easy_ to
achieve with this library, but there are some important considerations to note:

```kotlin
@Composable
fun SearchQueryHighlighting() {

    var searchQuery by remember { mutableStateOf("") }

    val highlightMatching = remember(searchQuery) {
        basicPatternAnnotation(
            pattern = searchQuery,
            literalPattern = true,
            spanStyle = SpanStyle(background = Color.Yellow)
        )
    }

    val highlightedText = textToHighlight.annotatedWith(
        patternAnnotation = highlightMatching,
        performanceStrategy = PerformanceStrategy.Performant
    )

    TextField(
        value = searchQuery,
        onValueChange = { searchQuery = it }
    )

    Text(highlightedText)
}
```

###### Result:

![Search result highlighting](images/search_result_highlighting.webp)

> [!CAUTION]
> If you are building a pattern annotation based on user input, make sure to set `literalPattern` to
> true - invalid regex will crash the app.

> [!WARNING]
> Note the methods to avoid excessive or slow re-compositions when using dynamic patterns:
> 1. Use `remember` to cache the `PatternAnnotation` with the dynamic pattern. This prevents the
     pattern from having to be instantiated and rebuilt on every recomposition.
> 2. When immediacy is not crucial, use the `PerformanceStrategy.Performant` option when calling
     `annotatedWith()` or `patternAnnotatedString()`. This means that text is styled in a
     background thread and leads to a *slight* delay in the styles becoming visible.

### Inline content

> Custom inline content, like rendering link previews, user pills, hashtags, etc.

Compose includes support for inline text content in `buildAnnotatedString` and the `Text`
Composable, but it can be cumbersome to use - and very difficult with dynamic text.

`String.patternAnnotatedString()` can easily build and return an
`inlineContentMap` that the `Text()` composable can use:

```kotlin
val usernameAnnotation = inlineContentPatternAnnotation(
    pattern = "@[A-Za-z0-9_]+",
    inlineContent = { matchedText ->
        inlineTextContent(width = 7.3.em, height = 1.8.em) {
            // You can use any composable here, but make sure it fits within the bounds you choose^
            UsernamePill(matchedText)
        }
    }
)

@Composable
fun SimpleInlineExample() {

    val styledComment = "Thanks @xavier, this is cool!"
        .patternAnnotatedString(usernameAnnotation)

    Text(
        text = styledComment.annotatedString,
        inlineContent = styledComment.inlineContentMap
    )
}
```

###### Result:

![Inline content example](images/inline_example.png)

> [!TIP]
> If you need to render inline content using, say, data fetched from an API, you can generate your
> pattern annotation in the Composable, but make sure to use `remember` with a key, such as in the
> search highlighting example, to avoid rebuilding the pattern on every recomposition.

### Basic paragraph styling

> Control text layout, line height, alignment

If you don't need to draw custom backgrounds behind paragraphs, paragraph styling is simple. You can
customise paragraph styles and easily align text, change line spacing, etc.

```kotlin
val rightAlignedBrackets = paragraphPatternAnnotation(
    pattern = "\\(.+\\)",
    paragraphStyle = ParagraphStyle(
        textAlign = TextAlign.End
    ),
    // You can also use SpanStyle here! Paragraph and Span styles can be combined.
    // spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
)

@Composable
fun ParagraphAlignmentExample() {
    val annotated = "I am left aligned\n(And I am right aligned)".patternAnnotatedString(
        patternAnnotation = rightAlignedBrackets
    )

    Text(
        text = annotated.annotatedString,
        modifier = Modifier.fillMaxWidth(),
    )
}
```

###### Result:

![Paragraph example](images/paragraph_alignment_example.png)

## Advanced paragraph styling with backgrounds

> Custom backgrounds behind paragraphs, as well as text styling and line height adjustments

It is not possible to draw custom backgrounds with `ParagraphStyle` - you can only change text
arrangement/layout properties. To fix this, `PatternAnnotatedString` includes a few helper
functions and APIs that make paragraph backgrounds easy to implement.

__Paragraph background styling steps:__

1. Create a `PatternAnnotation` using `paragraphPatternAnnotation()` with Paragraph styles and/or
   backgrounds.
2. Use `String.getPatternAnnotatedString()` to get a `PatternAnnotatedString` which
   includes background annotations.
3. Call `useParagraphBackgrounds` with the background annotations.
4. Pass the `annotatedString` and `useParagraphBackgrounds` result to a `Text` composable, using the
   `drawParagraphBackgrounds` modifier and the `onTextLayout` parameter.

```kotlin
val codeBlockAnnotation = paragraphPatternAnnotation(
    pattern = "```[^` ][^`]*[^ ]?```",
    spanStyle = SpanStyle(fontFamily = FontFamily.Monospace),
    paragraphStyle = ParagraphStyle(
        // Customise the text alignment, line spacing, etc.
    ),
    onDrawParagraphBackground = { rect ->
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

@Composable
fun ParagraphStyling() {

    val annotated = multiParagraphText.getPatternAnnotatedString(
        patternAnnotation = codeBlockAnnotation
    )

    val backgroundsResult = useParagraphBackgrounds(
        paragraphBackgroundAnnotations = annotated.paragraphBackgroundAnnotations
    )

    Text(
        text = annotated.annotatedString,
        onTextLayout = backgroundsResult.onTextLayout,
        modifier = Modifier
            .fillMaxWidth()
            .drawParagraphBackgrounds(backgroundsResult.backgroundsToDraw),
    )

}

```

###### Result:

![Paragraph example](images/paragraph_example.png)

> [!WARNING]
> There are a few things to keep in mind when styling paragraphs with this library:
> 1. Overlapping paragraphs will cause a crash! Ensure that your patterns are mutually exclusive.
> 2. Backgrounds are drawn on the main thread, so keep them simple.
> 3. Backgrounds only appear on the second re-composition, so keep this in mind when designing your
     UI.

### Multiple/combined annotations

You are not limited to one pattern annotation (or type of pattern annotation) when styling a
string - both `annotatedWith()` and `patternAnnotatedString()` can take a list of annotations.

This makes it easy to create rich, custom text styles:

```kotlin
@Composable
fun CombinedExample() {

    val userComment = "Thanks @xavier, this is cool!" +
            "I love _italics_ and would like to give you an apple to say thanks :)"

    val styledComment = userComment.patternAnnotatedString(
        patternAnnotations = listOf(usernameAnnotation, italics, redFruit)
    )

    Text(
        text = styledComment.annotatedString,
        inlineContent = styledComment.inlineContentMap
    )
}
```

###### Result:

![Combined example](images/mixed_example.png)

# More to come

- Performance notes
- API documentation
- Tips and tricks