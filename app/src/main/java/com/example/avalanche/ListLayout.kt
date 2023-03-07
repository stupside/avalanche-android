package com.example.avalanche

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.avalanche.ui.theme.AvalancheTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldTest(modifier: Modifier = Modifier, activityTitle: String) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        activityTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 34.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },
        content = { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                /*val list = (0..75).map { it.toString() }
                items(count = list.size) {
                    Text(
                        text = list[it],
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }*/
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* do something */ },
            ) {
                Icon(Icons.Filled.Add, "Localized description")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowPassElement(modifier: Modifier = Modifier, mainInfo: String, extraInfo: String) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        onClick = { /* do something */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row() {
            Image(
                painter = painterResource(id = R.drawable.yphejpfs_400x400),
                contentDescription = "7 Laux Logo"
            )
            Column {
                Text(text = mainInfo, fontSize = 20.sp)
                Text(text = extraInfo, fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreviewListLayout() {
    AvalancheTheme() {
        val activityTitle = "Wallet"
        ScaffoldTest(modifier = Modifier.fillMaxSize(), activityTitle = activityTitle)
    }
}