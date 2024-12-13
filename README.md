# PatternAnnotation

Easily style text using patterns/regular expressions in Jetpack Compose.

The library includes support for most of the features you might use in `buildAnnotatedString` with
`SpanStyle` and `ParagraphStyle`, while also including out-of-the-box support for custom paragraph
backgrounds and an easy way to manage inline Composable content, like rendering clickable username pills.

## Basic usage (text styling)
1. Create a `PatternAnnotation` with an associated style.
2. Use `string.annotated(patternStyles)` to apply the styles to a string.

```kotlin

val highlightFruit = basicPatternStyle(
    pattern = "(\\w*berry)|(\\w{0,}apple)",
    // you can change color, weight and other font styles here
    spanStyle = SpanStyle(color = Color.Red)
)

@Composable
fun BasicExample() {

    val textToStyle = "I like strawberries, carrots, and apples."

    val redFruit = textToStyle.annotated(
       with = listOf(highlightFruit)
    )

    Text(text = redFruit)
}
```

Result:


## Search highlighting

You can also build a PatternStyle 


## Paragraph styling

AnnotatedString only supports text placement changes with ParagraphStyle, making it hard to render paragraph level backgrounds or elements.
PatternAnnotation includes a lean but highly flexible approach to solving this problem.

Simply pass `drawParagraphBackground` to a `PatternAnnotation`, alongside other styles, and use the provided helper function to render it

## 





