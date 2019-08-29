package com.daovu65.studentmanager.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_table")
data class StudentEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    @ColumnInfo(name = "birth")
    val birth: Long,

    @ColumnInfo(name = "sex")
    val sex: String,

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "major")
    val major: String,

    @ColumnInfo(name = "image_profile")
    val imageProfile: String
)