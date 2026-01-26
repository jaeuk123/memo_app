package com.test.memoapp.memo.write

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.test.memoapp.R
import com.test.memoapp.core.Util.DateFormatUtils
import com.test.memoapp.core.component.customTextFieldColors
import com.test.memoapp.core.component.dialog.ConfirmTextDialog
import com.test.memoapp.core.component.dialog.TimeSelectDialog
import com.test.memoapp.core.component.topbar.SaveTopbar
import com.test.memoapp.memo.component.TagSelector
import com.test.memoapp.memo.data.TagEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun WriteScreen(itemId : Long ,onBackClick: () -> Unit, viewModel: WriteScreenViewModel = hiltViewModel()) {
    val title by viewModel.title.collectAsStateWithLifecycle()
    val dateText by viewModel.formattedDate.collectAsStateWithLifecycle()
    val content by viewModel.content.collectAsStateWithLifecycle()
    val todoOption by viewModel.todoOption.collectAsStateWithLifecycle()
    val scheduleTime by viewModel.scheduleTime.collectAsStateWithLifecycle()
    val scheduleOption by viewModel.scheduleOption.collectAsStateWithLifecycle()
    val allTags by viewModel.allTags.collectAsStateWithLifecycle()
//    val selectTags by viewModel.selectTags.collectAsStateWithLifecycle()

    val writeFormState = WriteFormState(
        title = title,
        dateText = dateText,
        content = content,
        todoOption = todoOption,
        scheduleTime = scheduleTime,
        scheduleOption = scheduleOption,
        allTags = allTags,
    )

    WriteContent(
        onBackClick = onBackClick,
        writeFormState,
        onAction = { action ->
            viewModel.handleAction(action)
        }
    )
}

@Composable
fun WriteContent(
    onBackClick: () -> Unit,
    writeFormState: WriteFormState,
    onAction: (EventAction) -> Unit,
) {
//    val addedOptionSchedule = remember { mutableStateOf(false) }
    val addedOptionTag = remember { mutableStateOf(false) }
    val showOptionTag = remember { mutableStateOf(false) }

    // 일정 피커 표시
    val showDatePickerModal = remember { mutableStateOf(false) }
    // 일정 시간 표시
    val showTimePickerModal = remember { mutableStateOf(false) }
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
            onAction(EventAction.saveEvent)
            onBackClick.invoke()
        })
    }) { paddingValues ->

        if (showDatePickerModal.value) {
            DatePickerDialog(
                onDismissRequest = { showDatePickerModal.value = false },
                confirmButton = {
                    TextButton(onClick = {
                        if (datePickerState.selectedDateMillis != null) {
                            onAction(EventAction.onDateChanged(datePickerState.selectedDateMillis!!))
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

        if (showTimePickerModal.value) {
            TimeSelectDialog({
                showTimePickerModal.value = !showTimePickerModal.value
            }) { time ->
                onAction(EventAction.onTimeSelected(time))
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
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            //제목 입력 부분
            TextField(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                value = writeFormState.title,
                onValueChange = { title -> onAction(EventAction.onTitleChanged(newTitle = title)) },
                colors = customTextFieldColors(Color.White),
                label = { Text(stringResource(R.string.default_string_title)) })

            TextField(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                value = writeFormState.content,
                onValueChange = { content -> onAction(EventAction.onContentChanged(newContent = content)) },
                colors = customTextFieldColors(Color.White),
                label = { Text("내용") })

            //메모 Option
            Column(
                modifier = Modifier.padding(start = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                //일정
                Row {
                    Column(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .width(IntrinsicSize.Max)
                            .animateContentSize()
                    ) {
                        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "일정",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.width(4.dp))
                                Switch(
                                    modifier = Modifier
                                        .scale(0.65f),
                                    checked = writeFormState.scheduleOption,
//                                        addedOptionSchedule.value
//                                    {onAction(EventAction.scheduleOptionChanged())},
                                    onCheckedChange = { value ->
                                        onAction(EventAction.scheduleOptionChanged(value))
//                                        addedOptionSchedule.value = !addedOptionSchedule.value
                                    })
                            }
                        }

                        if (writeFormState.scheduleOption) {
                            Column() {
                                //일정 날짜
                                Row(
                                    modifier = Modifier
                                        .clickable {
                                            showDatePickerModal.value = true
                                        }, verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = stringResource(
                                            R.string.default_string_schedule
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = writeFormState.dateText
                                    )
                                    Spacer(modifier = Modifier.width(30.dp))
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                //일정 시간
                                Row(
                                    modifier = Modifier
                                        .clickable {
                                            showTimePickerModal.value = true
                                        }
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AccessTime,
                                        contentDescription = "시간"
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = writeFormState.scheduleTime.format(DateFormatUtils.timeFormatPattern())
                                    )
                                }
                            }
                        }
                    }
                }

                //Todo Option
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Todo",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Switch(
                            checked = writeFormState.todoOption,
                            onCheckedChange = { check ->
                                onAction(
                                    EventAction.todoOptionChanged(
                                        check
                                    )
                                )
                            },
                            modifier = Modifier.scale(0.65f)
                        )
                    }
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "태그",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        IconButton(modifier = Modifier.padding(start = 6.dp),onClick = {}) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        }
                    }
                    TagSelector(writeFormState.allTags,) {

                    }
                }

            }

        }
    }
}

@Composable
@Preview
private fun PreviewWriteScreen() {
    val tagEntity = TagEntity(tagId = 1, tagName = "태그1")
    WriteContent(
        {}, writeFormState = WriteFormState(
            title = "제목",
            dateText = "2022-02-23",
            content = "내용",
            todoOption = false,
            scheduleTime = LocalTime.of(0, 0),
            false,
            listOf(tagEntity,tagEntity,tagEntity,tagEntity,tagEntity,tagEntity,tagEntity,tagEntity)
        ), {})
}

//
data class WriteFormState(
    val title: String,
    val dateText: String,
    val content: String,
    val todoOption: Boolean,
    val scheduleTime: LocalTime,
    val scheduleOption: Boolean,
    val allTags : List<TagEntity>
)

sealed class EventAction {
    object saveEvent : EventAction()
    data class onTitleChanged(val newTitle: String) : EventAction()
    data class onContentChanged(val newContent: String) : EventAction()
    data class onDateChanged(val date: Long) : EventAction()
    data class todoOptionChanged(val todoOption: Boolean) : EventAction()
    data class onTimeSelected(val time: LocalTime) : EventAction()
    data class scheduleOptionChanged(val scheduleOption: Boolean) : EventAction()
    data class addTags(val tagName: String) : EventAction()
}
