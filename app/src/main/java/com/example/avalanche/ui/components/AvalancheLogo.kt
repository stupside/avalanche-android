package com.example.avalanche.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.avalanche.R

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
        painter = painterResource(R.drawable.yphejpfs_400x400),
        contentDescription = "Placeholder",
        modifier = Modifier
            .size(size)
    )
}