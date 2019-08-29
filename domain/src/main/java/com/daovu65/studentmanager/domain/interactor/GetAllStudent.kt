package com.daovu65.studentmanager.domain.interactor

import com.daovu65.studentmanager.domain.entity.Student
import com.daovu65.studentmanager.domain.repository.StudentRepository

class GetAllStudent constructor(private val repo: StudentRepository) {
    suspend fun invoke(): List<Student> = repo.getAllStudentAsync().await()
}