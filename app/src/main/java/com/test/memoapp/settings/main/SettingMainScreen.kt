package com.test.memoapp.settings.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.test.memoapp.core.component.topbar.TitleBasicTopBar

@Composable
fun SettingMainScreen(loginNavigate: () -> Unit, viewmodel: SettingsViewModel = hiltViewModel()) {
    val isLogin = viewmodel.isLoggedIn.collectAsStateWithLifecycle()
    val email = viewmodel.email.collectAsStateWithLifecycle()

    SettingMainContents(
        loginNavigate,
        isLogin.value,
        email.value,
        { viewmodel.logout() }
    )
}

@Composable
fun SettingMainContents(
    loginNavigate: () -> Unit,
    isLogin: Boolean,
    email: String?,
    logout: () -> Unit
) {
    Scaffold(topBar = { TitleBasicTopBar("설정") }) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.LightGray
            )
            if (isLogin) {
                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .clickable {
                            logout.invoke()
                        }) {
                    Text("User Info : ${email}")
                    Text("로그 아웃", Modifier.align(Alignment.End))
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.LightGray
                )
            } else {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            loginNavigate.invoke()
                        }) {
                    Text("로그인")
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.LightGray
                )
            }

        }

    }
}

@Preview
@Composable
fun SettingMainPreview() {
    SettingMainContents({}, false, "a@naver.com", {})
}