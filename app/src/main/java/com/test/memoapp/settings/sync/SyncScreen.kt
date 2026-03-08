package com.test.memoapp.settings.sync

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.test.memoapp.core.component.topbar.TitleTopBar

@Composable
fun SyncScreen(backPressEvent: () -> Unit, viewModel: SyncViewModel = hiltViewModel()) {
    val memoSyncStatus = viewModel.syncStatus.collectAsStateWithLifecycle()
    val needSyncList = viewModel.needSyncList.collectAsStateWithLifecycle()

    Scaffold(topBar = { TitleTopBar(backPressEvent, "데이터 동기화") }) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            HorizontalDivider(thickness = 1.dp)
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(modifier = Modifier.align(Alignment.CenterStart), text = memoSyncStatus.value)
                Row(
                    Modifier.align(Alignment.CenterEnd)
                ) {
                    Button(onClick = {
                        viewModel.startSaveSync()
                    }, enabled = "동기화 대기 중..." == memoSyncStatus.value) {
                        Text("다운로드")
                    }
                    Spacer(Modifier.width(10.dp))
                }
            }

            HorizontalDivider(thickness = 1.dp)


            Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("데이터 업로드", modifier = Modifier.align(Alignment.CenterStart))

                Button(modifier = Modifier.align(Alignment.CenterEnd), onClick = {
                    viewModel.startUploadSync()
                }, enabled = needSyncList.value.isNotEmpty()) {
                    Text("${needSyncList.value.size}개 미반영사항 업로드")
                }
            }

            HorizontalDivider(thickness = 1.dp)

        }
    }
}

@Composable
@Preview
private fun PreviewSyncScreen() {
    SyncScreen({})
}