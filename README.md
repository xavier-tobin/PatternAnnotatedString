# PatternStyles for Jetpack Compose

Easily style text using pattern/regular expressions in Jetpack Compose.

The library includes support for the features you might use in `buildAnnotatedString` with
`SpanStyle` and `ParagraphStyle`, while also including out-of-the-box support for custom paragraph
backgrounds and an easy way to manage inline Composable content, like username handles.

# Usage

## Basic example

1. Create a `PatternAnnotation` which maps a pattern to styles.
2. Use `string.annotatedWith()` to create an `AnnotatedString` with the styles applied.

```kotlin
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
```

> [!NOTE]
> `annotatedWith` is a Composable function and only re-calculates styles if the text or
> annotations/s change.
> Many styles, long text or complicated patterns may impact performance, but the library includes
> options to cater for this - please see the Performance considerations section.



