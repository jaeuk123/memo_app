package com.test.memoapp.memo.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.test.memoapp.core.component.ExpandedMemoBox
import com.test.memoapp.memo.data.MemoEntity

@Composable
fun HomeScreen(viewModel : HomeListViewModel = hiltViewModel()) {
    val memos = viewModel.memos.collectAsStateWithLifecycle()
    HomeListContent(memos.value)
}

@Composable
fun HomeListContent(memos: List<MemoEntity>) {
    Scaffold { paddingValues ->
        Row(modifier = Modifier) {
            LazyColumn(modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(memos) { memo ->
                    ExpandedMemoBox(content = memo.title, timeStamp = memo.lastModifyTime)
                }
//                ExpandedMemoBox(content = )
            }
        }
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    HomeListContent(DummyItems.list)
}

object DummyItems {
    val single = MemoEntity(memoId = 1, scheduleTime = 171234567890L,title = "오늘의 할 일", lastModifyTime = 171234567890L)

    val list = listOf(
        single,
        single,
        single.copy(title = "a \n bd \n c")
    )
}