# PatternStyles for Jetpack Compose

Easily style text using pattern/regular expressions in Jetpack Compose.

The library includes support for the features you might use in `buildAnnotatedString` with
`SpanStyle` and `ParagraphStyle`, while also including out-of-the-box support for custom paragraph
backgrounds and an easy way to manage inline Composable content, like username handles.

## Basic usage
1. Create a `PatternStyle` which maps a pattern to some styles
2. Use `rememberPatternAnnotatedString()` to apply the styles to a string.

```kotlin

val highlightFruit = basicPatternStyle(
    pattern = "(\\w*berry)|(\\w{0,}apple)",
    spanStyle = SpanStyle(color = Color.Red)
)

@Composable
fun BasicExample() {

    val textToStyle = "I like strawberries, carrots, and apples."

    val redFruit = rememberPatternAnnotatedString(
        text = textToStyle,
        patternStyles = listOf(redFruit)
    )

    Text(text = redFruit)
}
```

Result:
// image

##




