package com.test.memoapp.memo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "memos")
data class MemoEntity(
    @PrimaryKey(autoGenerate = true)
    val memoId: Long = 0L,

    val title:String,
    val content: String = "",

    // 마지막 수정 시간
    val lastModifyTime : Long = System.currentTimeMillis(),
    // 일정 시간
    val scheduleTime : Long = 0L,
    // memoType
    val memoType : String
)

enum class MemoType(val value : String) {
    Default("D"), Schedule("S") , Todo("T")
}

class MemoTypeConverter {
    @TypeConverter
    fun fromType(type : MemoType) : String {
        return type.value
    }

    @TypeConverter
    fun toType(typeValue : String ) : MemoType {
        return MemoType.entries.find { it.value == typeValue } ?: MemoType.Default
    }
}
