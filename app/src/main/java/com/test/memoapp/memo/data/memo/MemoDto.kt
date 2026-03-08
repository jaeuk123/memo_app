package com.test.memoapp.memo.data.memo

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class MemoDto(
    @SerializedName("id")
    val memoId: String = UUID.randomUUID().toString(),
    @SerializedName("user_id")
    val userId : String = "",
    val title:String,
    val content: String = "",
    @SerializedName("last_modify_time")
    val lastModifyTime : Long = System.currentTimeMillis(),
    @SerializedName("is_deleted")
    val isDeleted : Boolean = false,
    @SerializedName("schedule_time")
    val scheduleTime : Long = 0L,
) {
    fun toEntity(isSync : Boolean) : MemoEntity {
        return MemoEntity(
            memoId = memoId ,
            title = title,
            content = content,
            lastModifyTime = lastModifyTime,
            scheduleTime = scheduleTime,
            isSync = isSync,
            isDelete = isDeleted
        )
    }
}