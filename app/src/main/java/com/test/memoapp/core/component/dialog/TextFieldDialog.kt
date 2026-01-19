package com.test.memoapp.core.component.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.test.memoapp.core.component.customTextFieldColors

@Composable
fun TextFieldSaveDialog(text: String, confirm: (String) -> Unit, onDismiss: () -> Unit) {
    var textFieldValue by remember { mutableStateOf("123") }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier,
        title = { Text(text = text) },
        text = {
            TextField(
                modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                value = textFieldValue,
                colors = customTextFieldColors(underLineColor = Color.Black),
                onValueChange = { textFieldValue = it }
            )
        },
        confirmButton = {
            TextButton(onClick = {confirm(textFieldValue)}) {
                Text("저장")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
    )
}

@Composable
@Preview
fun TextFieldDialogPreview() {
    TextFieldSaveDialog("테스트용 텍스트", {}, {})
}