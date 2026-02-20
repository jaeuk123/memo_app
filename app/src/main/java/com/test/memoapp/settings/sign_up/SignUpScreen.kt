package com.test.memoapp.settings.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.test.memoapp.core.component.topbar.TitleTopBar

@Composable
fun SignUpScreen(viewModel: SignUpViewModel = hiltViewModel(), navBackPress: () -> Unit) {
    SignUpContents({ email, password -> viewModel.signUp(email, password) }, navBackPress)
}

@Composable
private fun SignUpContents(signUp: (String, String) -> Unit, navBackPress: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(topBar = { TitleTopBar(navBackPress, "") }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .wrapContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("회원가입", fontWeight = FontWeight.Bold, fontSize = 27.sp)
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("이메일") })
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("비밀번호") })
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { signUp.invoke(email, password) },
//                enabled = uiState !is AuthUiState.Loading // 버튼 비활성화
            ) {
                Text("회원가입")
            }
        }
    }
}

@Composable
@Preview
private fun LoginScreenPreview() {
    SignUpContents({ email, password -> },{})
}