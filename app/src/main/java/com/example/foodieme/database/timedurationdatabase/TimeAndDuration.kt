package com.example.foodieme.database.timedurationdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodieme.domain.TimeDistance


@Entity(tableName = "time_duration_database")
data class TimeAndDuration(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "duration")
    var duration: Long? = 0L,

    @ColumnInfo(name = "distance")
    var distance: String? = ""
)

