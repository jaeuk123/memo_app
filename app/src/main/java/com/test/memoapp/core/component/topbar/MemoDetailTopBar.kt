package com.test.memoapp.core.component.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MemoDetailTopBar(
    backStackClick: () -> Unit,
    modifyOption: () -> Unit,
    removeOption: () -> Unit
) {
    var MenuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 4.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = backStackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back"
                )
            }
        }

        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
            IconButton(onClick = { MenuExpanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "")
            }

            DropdownMenu(
                expanded = MenuExpanded,
                onDismissRequest = { MenuExpanded = false }
            ) {
                DropdownMenuItem(onClick = {
                    modifyOption.invoke()
                }, text = { Text("수정") })
                DropdownMenuItem(onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.IO){
                            removeOption.invoke()
                        }
                        backStackClick.invoke()
                    }
                }, text = { Text("삭제") })
//                DropdownMenuItem(onClick = {
//                }, text = {Text("추가")})
            }
        }
    }
}

@Composable
@Preview
private fun PreviewTopBar() {
    MemoDetailTopBar({}, {}, {})
}