package com.test.memoapp.memo.data.memo

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


data class MemoUpdateDto (
    val title:String,
    val content: String,
    @SerializedName("last_modify_time")
    val lastModifyTime : Long = System.currentTimeMillis(),
    @SerializedName("is_deleted")
    val isDeleted : Boolean = false,
    @SerializedName("schedule_time")
    val scheduleTime : Long,
)
