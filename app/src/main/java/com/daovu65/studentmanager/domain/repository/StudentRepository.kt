package com.daovu65.studentmanager.domain.repository

import androidx.lifecycle.LiveData
import com.daovu65.studentmanager.domain.entity.Student
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job

interface StudentRepository {
    suspend fun getAllStudentAsync(): Deferred<List<Student>>

    suspend fun getStudentByIdAsync(id: Int): Deferred<Student>

    suspend fun findStudentByName(name: String): Deferred<List<Student>>

    suspend fun deleteStudent(student: Student): Job

    suspend fun addStudent(student: Student): Job

    suspend fun updateStudent(student: Student): Job
}