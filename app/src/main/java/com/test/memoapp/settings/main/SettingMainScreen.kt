package com.test.memoapp.settings.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingMainScreen(loginNavigate: () -> Unit) {
    SettingMainContents(loginNavigate)
}

@Composable
fun SettingMainContents(loginNavigate: () -> Unit) {
    Scaffold { paddingValues ->
        Column(modifier = Modifier.fillMaxWidth().padding(paddingValues)) {
            Row(Modifier.fillMaxWidth().padding(16.dp).clickable{
                loginNavigate.invoke()
            }) {
                Text("로그인")
            }
        }

    }
}

@Preview
@Composable
fun SettingMainPreview(){
    SettingMainContents({})
}