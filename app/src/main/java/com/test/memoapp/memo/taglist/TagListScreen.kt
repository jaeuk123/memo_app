package com.test.memoapp.memo.taglist

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
import com.test.memoapp.memo.component.TagsContents
import com.test.memoapp.memo.data.tag.TagEntity

@Composable
fun TagListScreen() {
//    val data =
//    TagListContent()
}

@Composable
fun TagListContent(data : List<TagEntity> = listOf<TagEntity>()) {
    Scaffold(topBar = {TitleTopBar({},"전체 태그 목록")}) { paddingValues ->
        Column(Modifier.padding(paddingValues).padding(start = 16.dp, end = 16.dp)) {
            LazyColumn {
                itemsIndexed(data) { index , item ->
                    TagsContents(item.tagName, getSectionShape(index,data.size), onClickEvent = {})
                }
            }
        }
    }
}


@Preview
@Composable
private fun PreviewTagListContent() {
    TagListContent(DummyItems.tagList)
}