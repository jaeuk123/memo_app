package com.test.memoapp.core.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSelectDialog(
    onDismissRequest : () -> Unit,
    onConfirm : (LocalTime) -> Unit,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = 12,
        initialMinute = 0,
        is24Hour = false,
    )

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(LocalTime.of(timePickerState.hour,timePickerState.minute))
                onDismissRequest()
            }) { Text("확인") }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) { Text("취소") }
        },
        text = {
            TimeInput(state = timePickerState)
        }
    )
}

@Preview
@Composable
private fun preview(){
    val k  by remember{mutableStateOf(false)}
    TimeSelectDialog({},{})
}