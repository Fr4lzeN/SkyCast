package com.example.skycast.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class City(
    @PrimaryKey
    @ColumnInfo(name = "city_id")
    val name: String,
    val sunrise: Long,
    val sunset: Long,
)
