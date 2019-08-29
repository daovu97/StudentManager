package com.daovu65.studentmanager.domain.interactor

import com.daovu65.studentmanager.domain.entity.Student
import com.daovu65.studentmanager.domain.repository.StudentRepository

class UpdateStudent constructor(private val repo: StudentRepository) {
    suspend fun invoke(student: Student) = repo.updateStudent(student)
}