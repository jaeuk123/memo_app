package com.test.memoapp.memo.data.tag

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class TagDto(
    @SerializedName("id")
    val tagId: String = UUID.randomUUID().toString(),
    @SerializedName("tag_name")
    val tagName: String,
    @SerializedName("user_id")
    val userId : String
) {
    fun toEntity(isSync : Boolean) : TagEntity {
        return TagEntity(
            tagId = tagId ,
            tagName = tagName,
            isSync = isSync
        )
    }
}