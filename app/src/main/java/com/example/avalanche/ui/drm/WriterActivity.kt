package com.example.avalanche.ui.drm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.avalanche.ui.theme.AvalancheTheme
import org.koin.androidx.compose.koinViewModel

class WriterActivity : ComponentActivity() {

    companion object {

        const val ChallengeIdKey = "ChallengeId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val challengeId: String? = intent.getStringExtra(ChallengeIdKey)!!

        setContent {

            AvalancheTheme {

                WriterView(koinViewModel(), { finish() }, challengeId)
            }
        }
    }
}