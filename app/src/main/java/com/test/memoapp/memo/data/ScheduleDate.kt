package com.test.memoapp.memo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule_dates")
data class ScheduleDate(
    @PrimaryKey val dateMillis : Long ,
    val timeMillis : Long ,
//    val isCompleted : Boolean,
//    val completedAt : Long?  = null
)

