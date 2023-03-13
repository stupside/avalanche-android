package com.example.avalanche.layouts

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.avalanche.R


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ScaffoldBase(modifier: Modifier = Modifier,
                              activityTitle: String,
                              floatingButton: Boolean,
                              scaffold_Content: List<Any>
    ) {
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
                    items(scaffold_Content) { scaffold_Content ->
                        //SectionElement(sectionTitle = , contentForSection = )
                    }
                }
            },
            floatingActionButton = {
                if (floatingButton) {
                    FloatingActionButton(
                        onClick = { /* do something */ },
                    ) {
                        Icon(Icons.Filled.Add, "Localized description")
                    }
                }
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SectionElement(
        sectionTitle: String,
        contentForSection: Map<String, String>
    ){
        Row() {
            Text(
                sectionTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 34.sp
            )
            LazyColumn(
                //contentPadding = ,
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
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RowPassElement(
        modifier: Modifier = Modifier,
        mainInfo: String,
        extraInfo: String,
        image_id: Int,
        image_description: String
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            onClick = { /* do something */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row() {
                Image(
                    painter = painterResource(id = image_id),
                    contentDescription = image_description
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
        val lauxRowElementDummyData = listOf(
            R.drawable.yphejpfs_400x400,
            mapOf(
                "mainInfo" to "Les 7 Laux",
                "extraInfo" to "1 pass"
            )
        )
        val inUseSectionDummyData = listOf(
            "In Use",
            lauxRowElementDummyData,
            lauxRowElementDummyData
        )
        val walletDummyData = listOf(
            inUseSectionDummyData,
            inUseSectionDummyData
        )
        val activityTitle = "Wallet"
        val floatingButton = true
        ScaffoldBase(
            modifier = Modifier.fillMaxSize(),
            activityTitle = activityTitle,
            floatingButton = floatingButton,
            scaffold_Content = walletDummyData
        )
    }
}