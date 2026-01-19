package com.test.memoapp.memo.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.test.memoapp.core.component.ExpandedMemoBox
import com.test.memoapp.core.component.dialog.TextFieldSaveDialog
import com.test.memoapp.memo.data.MemoEntity
import com.test.memoapp.memo.data.MemoType
import com.test.memoapp.memo.data.TagEntity
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(viewModel: HomeListViewModel = hiltViewModel()) {
    val memos = viewModel.memos.collectAsStateWithLifecycle()
    val tags = viewModel.tags.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val tagSaveEvent = { result: String ->
        viewModel.changeWriteTag(result)
        scope.launch {
            viewModel.saveTag()
        }
    }

    HomeListContent(memos.value, tags.value, { result -> tagSaveEvent.invoke(result) })
}

@Composable
fun HomeListContent(
    memos: List<MemoEntity>,
    tags: List<TagEntity>,
    tagSaveEvent: (String) -> Unit
) {
    Scaffold { paddingValues ->
        var tagDialogVisible by remember { mutableStateOf(false) }
        if (tagDialogVisible)
            TextFieldSaveDialog("새로운 태그", { result ->
                tagDialogVisible = false
                tagSaveEvent.invoke(result)
            }, {
                tagDialogVisible = false
            })
        Row(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            ) {
                //TodoSection
                stickyHeader {
                    ItemsTitleText("Todo", {})
                    Spacer(modifier = Modifier.height(5.dp))
                }
                itemsIndexed(memos) { index, item ->
                    val shape = getSectionShape(index, memos.size)
                    TodoContents(text = item.title, shape = shape)

                    if (index == memos.lastIndex) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                stickyHeader {
                    ItemsTitleText("Today Schedule", {} )
                }

                itemsIndexed(tags) { index, item ->
                    val shape = getSectionShape(index, tags.size)
                    ScheduleContents(text = item.tagName, shape = shape)

                    if (index == tags.lastIndex) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                //TagSection
                stickyHeader {
                    ItemsTitleText("Tags", {} , addOption = true , addClickEvent = { tagDialogVisible = true })
                }
                itemsIndexed(tags) { index, item ->
                    val shape = getSectionShape(index, tags.size)
                    TagsContents(text = item.tagName, shape = shape)

                    if (index == tags.lastIndex) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                stickyHeader {
                    ItemsTitleText("제목 없는 메모", {})
                }

                itemsIndexed(memos) { index, item ->
                    val shape = getSectionShape(index, tags.size)
                    MemoContents(text = item.title, shape = shape)

                    if (index == memos.lastIndex) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun MemoContents(text: String, shape: Shape) {
    ExpandedMemoBox(text)
//    Surface(
//        modifier = Modifier.fillMaxWidth(),
//        shape = shape,
//        color = Color.White,
//    ) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text(
//                text = text,
//                modifier = Modifier
//                    .padding(16.dp)
//                    .weight(1f)
//            )
//        }
//    }
}

@Composable
fun ScheduleContents(text: String, shape: Shape) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = shape,
        color = Color.White,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.padding(12.dp),
                fontWeight = FontWeight.Bold,
                text = "00:00",
            )
            Text(
                text = text,
                modifier = Modifier
                    .padding(6.dp, top = 16.dp, bottom = 16.dp)
                    .weight(1f)
            )
        }
    }
}

@Composable
fun TodoContents(text: String, shape: Shape) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = shape,
        color = Color.White,
    ) {
        var checked by remember { mutableStateOf(false) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            )
            Checkbox(checked = checked, onCheckedChange = { checked = !checked })
        }
    }
}

@Composable
fun TagsContents(text: String, shape: Shape) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = shape,
        color = Color.White,
    ) {
        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                modifier = Modifier.padding(4.dp),
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = ""
            )
        }
    }
}


@Composable
private fun ItemsTitleText(
    title: String,
    modifyClickEvent: () -> Unit,
    addClickEvent: () -> Unit = {},
    addOption: Boolean = false
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
        )
        if (addOption) {
            IconButton(onClick = addClickEvent) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                )
            }
        }

        IconButton(onClick = modifyClickEvent) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "",
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun getSectionShape(index: Int, totalSize: Int): Shape {
    val cornerRadius = 16.dp
    return when {
        totalSize == 1 -> RoundedCornerShape(cornerRadius)
        index == 0 -> RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius)
        index == totalSize - 1 -> RoundedCornerShape(
            bottomStart = cornerRadius,
            bottomEnd = cornerRadius
        )

        else -> RectangleShape
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    HomeListContent(DummyItems.todoList, DummyItems.tagList, {})
}

object DummyItems {
    val single = MemoEntity(
        memoId = 1,
        scheduleTime = 171234567890L,
        title = "오늘의 할 일",
        lastModifyTime = 171234567890L,
        memoType = MemoType.Default.value
    )

    val todoList = listOf(
        single,
        single,
        single.copy(title = "a \n bd \n c")
    )

    val tagList = listOf<TagEntity>(
        TagEntity(tagId = 1, tagName = "태그13"),
        TagEntity(tagId = 2, tagName = "태그13")
    )
}

