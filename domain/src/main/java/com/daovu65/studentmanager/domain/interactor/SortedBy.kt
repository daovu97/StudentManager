package com.daovu65.studentmanager.domain.interactor

import com.daovu65.studentmanager.domain.entity.Student
import com.daovu65.studentmanager.domain.repository.StudentRepository

class SortedBy constructor(private val repo: StudentRepository) {
    suspend fun invoke(value: String): List<Student> = repo.sortedByAsync(value).await()

}