package com.xaviertobin.patternstyles.compose.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun DemoSection(demoName: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(demoName, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        content()
    }
}

@Composable
fun ScrollPreviewLayout(
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(0.dp))
            .width(450.dp)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 30.dp)

    ) {
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
