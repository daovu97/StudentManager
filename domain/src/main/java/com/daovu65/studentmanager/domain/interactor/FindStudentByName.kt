package com.daovu65.studentmanager.domain.interactor

import com.daovu65.studentmanager.domain.entity.Student
import com.daovu65.studentmanager.domain.repository.StudentRepository

class FindStudentByName constructor(private val repo: StudentRepository) {
    suspend fun invoke(name: String): List<Student> = repo.findStudentByNameAsync(name).await()
}