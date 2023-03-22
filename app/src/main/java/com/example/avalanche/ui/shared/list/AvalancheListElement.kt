package com.example.avalanche.ui.shared.list

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun AvalancheListElement(
    image: String,
    description: String,
    content: @Composable () -> Unit,
    onClick: (() -> Unit)?
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        onClick = {
            onClick?.invoke()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row {

            val bytes = Base64.decode(image, Base64.DEFAULT)

            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, image.length)

            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = description
            )

            Column {
                content()
            }
        }
    }
}