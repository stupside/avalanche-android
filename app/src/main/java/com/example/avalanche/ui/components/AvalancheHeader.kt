package com.example.avalanche.shared.ui.compose

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AvalancheHeader(
    name: String,
    description: String?,
    logo: String?
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AvalancheLogo(logo)
                Spacer(modifier = Modifier.padding(ButtonDefaults.IconSpacing))
                Text(name, style = MaterialTheme.typography.titleMedium)
            }

            description?.let {
                Row {
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(it)
                }
            }

        }
    }
}

@Composable
fun AvalancheLogo(logo: String?, size: Dp = 64.dp) {
    if (logo == null) {
        AvalancheLogoPlaceholder(size)
    } else {

        val bytes = logo.toByteArray()

        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, logo.length)

        if (bitmap == null) {
            AvalancheLogoPlaceholder(size)
        } else {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = logo,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
fun AvalancheLogoPlaceholder(size: Dp) {
    Icon(
        painter = painterResource(com.example.avalanche.R.drawable.ic_launcher_foreground),
        contentDescription = "Placeholder",
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
    )
}