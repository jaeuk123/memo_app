package com.test.memoapp.data.Memo

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "memos")
data class MemoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title:String,
    val content: String,

    // 마지막 수정시간
    val lastModifyTime : Long = System.currentTimeMillis(),
    // 일정 시간
    val scheduleTime : Long,
)