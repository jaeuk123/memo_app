package com.test.memoapp.memo.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.test.memoapp.core.Util.DateConvertType
import com.test.memoapp.core.Util.DateFormatUtils
import com.test.memoapp.core.component.topbar.MemoDetailTopBar
import com.test.memoapp.memo.Utils.DummyItems
import com.test.memoapp.memo.data.memo.MemoEntity
import com.test.memoapp.memo.data.memo_tag_relation.MemoWithTags

@Composable
fun MemoDetailScreen(
    onBackClick: () -> Unit,
    navigateModify: (String) -> Unit,
    memoId: String,
    viewModel: MemoDetailViewModel = hiltViewModel()
) {
    val selectedTags = viewModel.MemoWithTags.collectAsStateWithLifecycle()

    val data = selectedTags.value ?: run {
        onBackClick
        return
    }

    DetailContent(
        data,
        onBackClick,
        { navigateModify(memoId) },
        { viewModel.removeMemo(memoId) })
}

@Composable
fun DetailContent(
    memoWithTags: MemoWithTags,
    onBackClick: () -> Unit,
    navigateModify: () -> Unit,
    removeMemo: () -> Unit,
) {
    Scaffold(topBar = {
        MemoDetailTopBar(
            backStackClick = onBackClick,
            navigateModify,
            removeMemo
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                if (memoWithTags.tags.isNotEmpty())
                    Text("태그 :", modifier = Modifier.padding(4.dp))
                for (entity in memoWithTags.tags) {
                    Text(entity.tagName, modifier = Modifier.padding(4.dp))
                }
            }
            Row {
                MemoDetailContentBox(memoWithTags.memo)
            }
        }
    }
}

@Composable
private fun FilterOutBox() {
    Surface(
        modifier = Modifier.border(
            width = 2.dp,
            color = Color.White,
            shape = RoundedCornerShape(4.dp)
        )
    ) {
    }
}

@Composable
fun MemoDetailContentBox(memo: MemoEntity) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    memo.title,
                    modifier = Modifier.align(Alignment.CenterStart),
                    fontWeight = FontWeight.Bold
                )

                if (memo.scheduleTime != 0L) {
                    Text(
                        DateFormatUtils.convertLongToString(
                            memo.scheduleTime,
                            type = DateConvertType.DATETIME
                        ), modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
            Text(memo.content)
        }
    }
}

@Composable
@Preview
private fun previewDetail() {
    DetailContent(
        MemoWithTags(
            DummyItems.memo, DummyItems.tagList
        ), {}, {}, {})
}