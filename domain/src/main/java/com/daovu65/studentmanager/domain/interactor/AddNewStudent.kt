package com.daovu65.studentmanager.domain.interactor

import com.daovu65.studentmanager.domain.entity.Student
import com.daovu65.studentmanager.domain.repository.StudentRepository

class AddNewStudent constructor(private val repo: StudentRepository) {
    suspend fun invoke(student: Student) {
        when {
            student.firstName.isNotEmpty() || student.firstName.isNotBlank() ->
                repo.addStudent(
                    Student(
                        id = 0,
                        firstName = student.firstName,
                        lastName = student.lastName,
                        birth = student.birth,
                        sex = student.sex,
                        address = student.address,
                        major = student.major,
                        imageProfile = student.imageProfile
                    )
                )
        }
    }
}