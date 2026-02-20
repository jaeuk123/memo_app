package com.test.memoapp.memo.data.tag

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey
    val tagId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "tag_name")
    val tagName: String,

    val isDelete : Boolean = false,
    val needSync : Boolean = false
)