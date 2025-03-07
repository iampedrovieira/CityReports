package com.example.cityreports.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="note_table")
class Note (
    @PrimaryKey(autoGenerate = true) val id:Int? = null,
    @ColumnInfo(name="description") val description:String
)