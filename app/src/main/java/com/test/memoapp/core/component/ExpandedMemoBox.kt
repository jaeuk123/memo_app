package com.test.memoapp.core.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.test.memoapp.core.Util.DateConvertType
import com.test.memoapp.core.Util.DateUtils

@Composable
fun ExpandedMemoBox(content: String) {
    var isExpanded by remember { mutableStateOf(false) }
    var showMoreButton by remember { mutableStateOf(false) }
    var showExtraInfo by remember { mutableStateOf(false) }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp, bottom = 2.dp)
                .animateContentSize(),
            contentAlignment = Alignment.TopEnd
        ) {
//            if (showExtraInfo)
//                Text(
//                    style = MaterialTheme.typography.bodySmall,
//                    text = DateUtils.convertLongToString(
//                        time = createTime,
//                        type = DateConvertType.DEFAULT
//                    )
//                )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showExtraInfo = !showExtraInfo
                },
        ) {
            Column(
                modifier = Modifier
                    .background(color = Color.White)
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
                    .animateContentSize()
            ) {
                //수정 삭제
                if (showExtraInfo) {
                    Row(modifier = Modifier.fillMaxWidth(), Arrangement.End) {
                        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Default.BorderColor,
                                    contentDescription = ""
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            IconButton(onClick = {}) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                            }
                        }
                    }
                }
                Row {
                    Text(
                        text = content,
                        maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis,
                        onTextLayout = { result ->
                            if (!isExpanded) {
                                showMoreButton = result.didOverflowHeight
                            }
                        }
                    )

                    if (showMoreButton) {
                        (Spacer(modifier = Modifier.weight(1f)))
                        IconButton(modifier = Modifier.align(Alignment.Bottom), onClick = {
                            isExpanded = !isExpanded
                        }) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Icon(
                                    imageVector =
                                        if (!isExpanded)
                                            Icons.Default.KeyboardArrowDown
                                        else
                                            Icons.Default.KeyboardArrowUp,
                                    contentDescription =
                                        if (!isExpanded)
                                            "더보기"
                                        else
                                            "접기"
                                )

                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewBox() {
    ExpandedMemoBox("내용 \n 1234 \n aaa ")
}