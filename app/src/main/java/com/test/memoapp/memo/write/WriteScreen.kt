package com.test.memoapp.memo.write

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.test.memoapp.R
import com.test.memoapp.core.component.customTextFieldColors
import com.test.memoapp.core.component.dialog.ConfirmTextDialog
import com.test.memoapp.core.component.topbar.SaveTopbar
import com.test.memoapp.memo.write.WriteScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun WriteScreen(onBackClick: () -> Unit, viewModel: WriteScreenViewModel = hiltViewModel()) {
    val title by viewModel.title.collectAsStateWithLifecycle()
    val dateText by viewModel.formattedDate.collectAsStateWithLifecycle()
    val scope  = rememberCoroutineScope()

    WriteContent(
        onBackClick,
        title,
        dateText,
        onDateChanged = viewModel::onDateSelected,
        onTitleChanged = viewModel::onTitleChanged,
        saveEvent = { scope.launch {
            viewModel.saveMemo()
        }}
    )
}

@Composable
fun WriteContent(
    onBackClick: () -> Unit,
    title: String,
    dateText: String,
    onTitleChanged: (String) -> Unit,
    onDateChanged: (Long) -> Unit,
    saveEvent: () -> Unit,
) {

    val addedOptionSchedule = remember { mutableStateOf(false) }
    val addedOptionTag = remember { mutableStateOf(false) }

    val showOptionTag = remember { mutableStateOf(false) }
    // 일정 피커 표시
    val showDatePickerModal = remember { mutableStateOf(false) }
    // 백프레스 다이얼로그 표시
    val showBackPressDialog = remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)

    BackHandler {
        showBackPressDialog.value = true
    }

    Scaffold(topBar = {
        SaveTopbar({
            showBackPressDialog.value = true
        }, {
            saveEvent.invoke()
            println("save button click")
            onBackClick.invoke()
        })
    }) { paddingValues ->

        if (showDatePickerModal.value) {
            DatePickerDialog(
                onDismissRequest = { showDatePickerModal.value = false },
                confirmButton = {
                    TextButton(onClick = {
                        if (datePickerState.selectedDateMillis != null) {
                            onDateChanged.invoke(datePickerState.selectedDateMillis!!)
//                            viewModel.onDateSelected(newTimestamp = datePickerState.selectedDateMillis)
                            addedOptionSchedule.value = true
                        }
                        showDatePickerModal.value = false
                    }) {
                        Text(stringResource(R.string.default_string_confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePickerModal.value = false }) {
                        Text(stringResource(R.string.default_string_cancel))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (showBackPressDialog.value) {
            ConfirmTextDialog(
                "작성을 취소하고 뒤로 가시겠습니까?",
                onDismiss = { showBackPressDialog.value = false },
                confirm = {
                    showBackPressDialog.value = false
                    onBackClick.invoke()
                })
        }

        Column(
            Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxWidth()
        ) {
            //제목 입력 부분
            TextField(
                modifier = Modifier
                    .padding(16.dp, 8.dp, 16.dp, 8.dp)
                    .fillMaxWidth(),
                value = title,
//                onValueChange = viewModel::onTitleChanged,
                onValueChange = onTitleChanged,
                colors = customTextFieldColors(Color.White),
                label = { Text(stringResource(R.string.default_string_title)) })

            //일정
            Row(modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 8.dp)) {
                if (!addedOptionSchedule.value)
                    Button(onClick = { showDatePickerModal.value = true }) {
                        Text(stringResource(R.string.default_string_add_schedule))
                    }
                else {
                    Row(modifier = Modifier
                        .clickable {
                            showDatePickerModal.value = true
                        }
                        .padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = stringResource(
                                R.string.default_string_schedule
                            )
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = dateText
                        )
                    }
                }
            }
            //태그
//            Row() {
//                TextField(modifier = Modifier, state = rememberTextFieldState(), label = {
//                    Text(
//                        stringResource(R.string.default_string_tag)
//                    )
//                })
//            }
        }
    }
}

@Composable
@Preview
fun PreviewWriteScreen() {
    WriteContent({}, "TITLE", "2025-12-25", {}, {},{})
}