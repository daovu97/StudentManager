package com.daovu65.studentmanager.domain.interactor

import com.daovu65.studentmanager.domain.entity.Student
import com.daovu65.studentmanager.domain.repository.StudentRepository

class GetStudentById constructor(private val repo:StudentRepository){
    suspend fun invoke(id:Int):Student = repo.getStudentByIdAsync(id).await()
}