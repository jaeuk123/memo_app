package com.test.memoapp.memo.memolist

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.test.memoapp.core.component.topbar.TitleTopBar
import com.test.memoapp.memo.Utils.DummyItems
import com.test.memoapp.memo.Utils.getSectionShape
import com.test.memoapp.memo.component.LastWriteMemoContents
import com.test.memoapp.memo.data.memo.MemoEntity

@Composable
fun MemoListScreen(
    onBackClick: () -> Unit,
    navigateDetail: (String) -> Unit,
    navigateModify : (String) -> Unit,
    tagId: String,
    viewModel: MemoListViewModel = hiltViewModel()
) {
    val tagWithMemos = viewModel.tagWithMemos.collectAsStateWithLifecycle()

    val items = tagWithMemos.value?.items ?: run {
        onBackClick
        return
    }
    val title = tagWithMemos.value?.tag?.tagName ?: run {
        onBackClick
        return
    }

    MemoListContent(items, title, navigateDetail , navigateModify, onBackClick = onBackClick)
}

@Composable
private fun MemoListContent(
    data: List<MemoEntity>,
    title: String,
    navigateDetail: (String) -> Unit,
    navigateModify: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var modifyOnItem by remember { mutableStateOf<String?>(null) }

    Scaffold(topBar = { TitleTopBar(onBackClick, title) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            LazyColumn {
                itemsIndexed(data) { index, memo ->
                    Box(
                        modifier = Modifier.combinedClickable(
                            true,
                            onClick = { navigateDetail(memo.memoId) },
                            onLongClick = {
                                modifyOnItem = memo.memoId
                            })
                    ) {
                        LastWriteMemoContents(
                            memo.title,
                            time = memo.scheduleTime,
                            isVisible = (memo.memoId == modifyOnItem),
                            shape = getSectionShape(index, data.size),
                            navigateModify = { navigateModify(memo.memoId) })
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun PreviewMemoListContent() {
    MemoListContent(DummyItems.memoList, "1234", {},{},{} )
}