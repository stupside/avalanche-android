package com.example.avalanche.core.ui.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.avalanche.core.ui.theme.AvalancheTheme

@Composable
fun AvalancheSection(
    title: String,
    content: @Composable () -> Unit
) {
    Row (modifier = Modifier.fillMaxWidth()) {

        Column {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 34.sp
            )

            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreviewListLayout() {
    AvalancheTheme {
        val used = "Used"
        AvalancheSection(title = used) {
        }
    }
}
