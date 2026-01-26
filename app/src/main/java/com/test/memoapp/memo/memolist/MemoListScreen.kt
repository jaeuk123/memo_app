package com.test.memoapp.memo.memolist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.test.memoapp.core.component.topbar.TitleTopBar
import com.test.memoapp.memo.Utils.DummyItems
import com.test.memoapp.memo.Utils.getSectionShape
import com.test.memoapp.memo.component.ScheduleContents
import com.test.memoapp.memo.data.MemoEntity

@Composable
fun MemoListScreen() {
//    MemoListContent()
}

@Composable
private fun MemoListContent(data: List<MemoEntity>) {
    Scaffold(topBar = { TitleTopBar({}, "태그명") }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(start = 16.dp, end = 16.dp)) {
            LazyColumn {
                itemsIndexed(data) { index, memo ->
                    ScheduleContents(memo.title, time = 0L , isVisible = false, shape = getSectionShape(index,data.size), navigateModify = {})
                }
            }
        }
    }
}

@Composable
fun TagMemoContent(title : String , ) {

}

@Composable
@Preview
private fun PreviewMemoListContent() {
    MemoListContent(DummyItems.memoList)
}