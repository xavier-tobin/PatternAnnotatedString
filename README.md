# PatternAnnotatedString

Easily and dynamically style text using pattern/regular expressions in Jetpack Compose.

- [x] 📝 Alternative to `buildAnnotatedString` for dynamic text
- [x] 🧩 Supports inline styles
- [x] 🎨 Highly flexible, simple Compose-ready API
- [x] 🚀 Respects compose lifecycle with performance options
- [x] 📦 Out-of-the-box support for custom paragraph backgrounds

# Usage

## Basic example

1. Create a `PatternAnnotation` (or multiple) which associates a pattern to some styles.
2. Use `string.annotatedWith()` to create an `AnnotatedString` with the style/s applied.

```kotlin
val redFruit = basicPatternAnnotation(
    pattern = "(\\w*berry)|(\\w{0,}apple)",
    spanStyle = SpanStyle(color = Color.Red)
)

@Preview
@Composable
fun BasicExample() {
    Text(
        text = "Strawberry Fridge Apple Ferrari".annotatedWith(redFruit)
    )
}
```

*Result*



> [!NOTE]
> `annotatedWith` is a Composable function and only re-calculates styles if the text or
> annotations/s change.
> Many annotations, long text or complex patterns may impact performance, but the library includes
> options to cater for this - please see the Performance considerations section.

## Search text highlighting (& other dynamic patterns)

There are use-cases for pattern-based styling where you may not know the pattern at compile-time.
For example, you may want to highlight matching text based on a search query the user inputs.

This is easy to achieve with this library, but there are some performance considerations.

1. Create a `PatternAnnotation` with a dynamic pattern, wrapped in a `remember` block.
2. Use `string.annotatedWith()` to apply the style/s to a string with the
   `PerformanceStrategy.Performant` option.

```kotlin
@Preview
@Composable
fun SearchTextHighlighting() {

    var searchQuery by remember { mutableStateOf("") }

    val highlightAnnotation = remember(searchQuery) {
        basicPatternAnnotation(
            pattern = searchQuery,
            spanStyle = SpanStyle(
                background = Color.Yellow,
            )
        )
    }

    val highlightedText = textToHighlight.annotatedWith(
        patternAnnotation = highlightAnnotation,
        performanceStrategy = PerformanceStrategy.Performant
    )


    TextField(
        value = searchQuery,
        onValueChange = { searchQuery = it }
    )

    Text(
        text = highlightedText,
        modifier = Modifier.padding(top = 10.dp)
    )

}
```

> [!NOTE]
> Note the two methods to avoid slow re-compositions when using dynamic patterns:
> 1. Use `remember` to cache the `PatternAnnotation` with the dynamic pattern. This prevents the
     pattern from having to be instantiated and rebuilt on every recomposition.
> 2. Use the `PerformanceStrategy.Performant` option when calling `annotatedWith`. This will
     mean that text is styled in a background thread, and lead to a *slight* delay in the styles being visible.


