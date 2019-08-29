package com.daovu65.studentmanager.domain.entity

data class Student(
    val id: Int,
    val imageProfile: String,
    val firstName: String,
    val lastName: String,
    val birth: Long,
    val sex: Int,
    val address: String,
    val major: String
)