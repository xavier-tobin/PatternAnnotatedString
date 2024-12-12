package com.xaviertobin.patternstyles.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xaviertobin.noted.compose.components.core.ScrollPreviewLayout
import com.xaviertobin.noted.compose.patternstyle.demo.BasicExample


@Composable
fun DemoSection(demoName: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(demoName, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        content()
    }
}

@Preview
@Composable
fun StyledPatternDemos() {
    ScrollPreviewLayout {
        DemoSection("Basic styling") {
            BasicExample()
        }

        DemoSection("Search highlighting") {
            SearchTextHighlighting()
        }
    }
}
