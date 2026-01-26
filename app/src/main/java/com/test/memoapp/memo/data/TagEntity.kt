package com.test.memoapp.memo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = true)
    val tagId: Long = 0,
    @ColumnInfo(name = "tag_name")
    val tagName: String
)