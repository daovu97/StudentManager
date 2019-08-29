package com.daovu65.studentmanager.data.maper

import com.daovu65.studentmanager.data.database.StudentEntity
import com.daovu65.studentmanager.domain.entity.Student

object Mapper {
    fun studentToStudentEntity(student: Student): StudentEntity =
        StudentEntity(
            id = student.id,
            firstName = student.firstName,
            lastName = student.lastName,
            birth = student.birth,
            sex = student.sex.toString(),
            address = student.address,
            major = student.major,
            imageProfile = student.imageProfile
        )

    fun studentEntityToStudent(studentEntity: StudentEntity): Student =
        Student(
            id = studentEntity.id,
            firstName = studentEntity.firstName,
            lastName = studentEntity.lastName,
            birth = studentEntity.birth,
            sex = Integer.parseInt(studentEntity.sex),
            address = studentEntity.address,
            major = studentEntity.major,
            imageProfile = studentEntity.imageProfile
        )

}