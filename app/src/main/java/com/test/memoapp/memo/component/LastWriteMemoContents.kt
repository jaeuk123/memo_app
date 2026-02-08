package com.test.memoapp.memo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.test.memoapp.core.Util.DateConvertType
import com.test.memoapp.core.Util.DateFormatUtils
import com.test.memoapp.core.Util.DateFormatUtils.DateFormatPattern

@Composable
fun LastWriteMemoContents(
    text: String,
    time: Long?,
    shape: Shape,
    isVisible: Boolean,
    navigateModify: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = shape,
        color = Color.White
    ) {
        var dateText : String = ""
        if (time != null && time != 0L) {
            dateText = DateFormatUtils.convertLongToString(time, DateConvertType.DEFAULT)
        }

        Box {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    modifier = Modifier.padding(12.dp),
                    fontWeight = FontWeight.Bold,
                    text = dateText,
                )
                Text(
                    text = text,
                    modifier = Modifier
                        .padding(6.dp, top = 16.dp, bottom = 16.dp)
                        .weight(1f)
                )
            }
            if (isVisible) {
                Row(modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()) {
                    IconButton(
                        modifier = Modifier
                            .background(color = Color(0xFF81D4FA))
                            .fillMaxHeight(),
                        onClick = {
                            navigateModify.invoke()
                        }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    }
                    IconButton(
                        modifier = Modifier
                            .background(color = Color(0xFFFFB7B2))
                            .fillMaxHeight(),
                        onClick = {}) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun PreviewSchedule() {
    ScheduleContents(text = "제목" , isVisible = true, time = 0L, navigateModify = {},shape = RoundedCornerShape(16.dp),)
}