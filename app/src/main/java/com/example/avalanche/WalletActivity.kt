package com.example.avalanche

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.avalanche.ui.shared.scaffold.AvalancheScaffold


class WalletActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AvalancheScaffold(activity = this, content = {

            }, button = {
                FloatingActionButton(
                    onClick = {},
                    shape = RoundedCornerShape(16.dp),
                ){
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add FAB",
                        tint = Color.White,
                    )
                }
            })
        }
    }
}

