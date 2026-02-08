package com.test.memoapp.memo.lastwriteList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.test.memoapp.core.component.topbar.TitleTopBar
import com.test.memoapp.memo.Utils.DummyItems
import com.test.memoapp.memo.Utils.getSectionShape
import com.test.memoapp.memo.component.LastWriteMemoContents
import com.test.memoapp.memo.data.MemoEntity
import kotlinx.coroutines.flow.flowOf

@Composable
fun LastWriteMemoListScreen(
    onBackClick: () -> Unit,
    navigateDetail: (Long) -> Unit,
    viewModel: LastWriteMemoViewModel = hiltViewModel()
) {
    val memoList = viewModel.memoPagingData.collectAsLazyPagingItems()

    LastWriteMemoListContents(memoList, navigateDetail,onBackClick)
}

@Composable
fun LastWriteMemoListContents(
    itemSnapshotList: LazyPagingItems<MemoEntity>,
    navigateDetail: (Long) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(topBar = { TitleTopBar(backStackClick = onBackClick, title = "작성한 메모") }) { paddingValues ->
        Column(Modifier
            .padding(paddingValues)
            .padding(start = 16.dp, end = 16.dp)) {
            LazyColumn {
                items(
                    count = itemSnapshotList.itemCount,
                    key = itemSnapshotList.itemKey { it.memoId }) { index ->
                    val shape = getSectionShape(index, itemSnapshotList.itemCount)
                    val memo = itemSnapshotList[index]
                    if (memo != null) {
                        Surface(
                            onClick = { navigateDetail(memo.memoId) }
                        ) {
                            LastWriteMemoContents(
                                text = memo.title,
                                isVisible = false,
                                navigateModify = {},
                                shape = shape,
                                time = memo.scheduleTime
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun previewListWriteScreen() {
    val flowOf = flowOf(PagingData.from(DummyItems.memoList))
    val collectAsLazyPagingItems = flowOf.collectAsLazyPagingItems()
    LastWriteMemoListContents(collectAsLazyPagingItems, {}, {})
}