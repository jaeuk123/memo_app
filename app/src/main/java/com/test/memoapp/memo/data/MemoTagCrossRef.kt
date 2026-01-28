package com.test.memoapp.memo.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.Relation

@Entity(
    tableName="memo_tag_cross_ref",
    primaryKeys = ["memoId","tagId"],
    foreignKeys = [
        ForeignKey(
            entity = MemoEntity::class,
            parentColumns = ["memoId"],
            childColumns = ["memoId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["tagId"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class MemoTagCrossRef(
    val memoId: Long,
    val tagId: Long
)



data class MemoWithTags(
    @Embedded val memo: MemoEntity,
    @Relation(
        parentColumn = "memoId",
        entityColumn = "tagId",
        associateBy = Junction(MemoTagCrossRef::class)
    )
    val tags: List<TagEntity>
)

data class TagWithMemos(
    @Embedded val tag: TagEntity,
    @Relation(
        parentColumn = "tagId",
        entityColumn = "memoId",
        associateBy = Junction(MemoTagCrossRef::class) // 중개 테이블 지정
    )
    val items: List<MemoEntity>
)