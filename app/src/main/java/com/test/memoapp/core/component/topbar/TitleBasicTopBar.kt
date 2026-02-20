package com.test.memoapp.core.component.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleBasicTopBar(title : String) {
    TopAppBar(
        title = { Text(text = title, fontWeight = FontWeight.Bold) },
    )
}

@Composable
@Preview
private fun preview(){
    TitleBasicTopBar("제목")
}