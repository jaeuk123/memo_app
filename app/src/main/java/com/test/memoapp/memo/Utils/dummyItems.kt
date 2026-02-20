package com.test.memoapp.memo.Utils

import com.test.memoapp.memo.data.memo.MemoEntity
import com.test.memoapp.memo.data.tag.TagEntity

object DummyItems {

    val tag = TagEntity(
        tagId = "1",
        tagName = "태그 1"
    )

    val tagList = listOf<TagEntity>(
        TagEntity(tagId = "1", tagName = "태그1"),
        TagEntity(tagId = "2", tagName = "태그2"),
        TagEntity(tagId = "3", tagName = "태그3")
    )

    val  selected = mutableSetOf<String>("1","3")

    val memo = MemoEntity(
//        memoId = 1,
        scheduleTime = 171234567890L,
        title = "오늘의 할 일",
        lastModifyTime = 171234567890L,
//        memoType = MemoType.Default.value
    )

    val memoList = listOf(
        memo,
        memo,
        memo.copy(title = "a \n bd \n c")
    )
}

