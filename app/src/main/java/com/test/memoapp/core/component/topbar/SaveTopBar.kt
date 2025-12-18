package com.test.memoapp.core.component.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveTopbar(
    backStackClick: () -> Unit,
    actions: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(text = "") },
        navigationIcon = {
            IconButton(onClick = backStackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            TextButton(onClick = actions) {
                Text(
                    text = "저장",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black
                )
            }
        },
    )
}

@Preview
@Composable
fun previewSaveTopBar() {
    SaveTopbar({},{})
}