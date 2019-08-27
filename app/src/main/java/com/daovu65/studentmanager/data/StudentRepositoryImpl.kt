package com.daovu65.studentmanager.data

import androidx.sqlite.db.SimpleSQLiteQuery
import com.daovu65.studentmanager.data.database.StudentDao
import com.daovu65.studentmanager.data.maper.Mapper
import com.daovu65.studentmanager.domain.entity.Student
import com.daovu65.studentmanager.domain.repository.StudentRepository
import kotlinx.coroutines.*

class StudentRepositoryImpl(
    private val studentDao: StudentDao
) : StudentRepository {
    override suspend fun sortedByAsync(value: String): Deferred<List<Student>> =
        withContext(Dispatchers.IO) {
            async {
                val query = "SELECT * FROM student_table ORDER BY $value ASC"
                studentDao.sortedBy(SimpleSQLiteQuery(query)).map { studentEntity ->
                    Mapper.studentEntityToStudent(studentEntity)
                }

            }
        }

    override suspend fun findStudentByNameAsync(name: String): Deferred<List<Student>> =
        withContext(Dispatchers.IO) {
            async {
                studentDao.findUserWithName(name).map { studentEntity ->
                    Mapper.studentEntityToStudent(studentEntity)
                }

            }
        }

    override suspend fun getAllStudentAsync(): Deferred<List<Student>> =
        withContext(Dispatchers.IO) {
            async {
                studentDao.getAllStudent().map { studentEntity ->
                    Mapper.studentEntityToStudent(studentEntity)
                }

            }
        }


    override suspend fun getStudentByIdAsync(id: Int): Deferred<Student> =
        withContext(Dispatchers.IO) {
            async {
                Mapper.studentEntityToStudent(studentDao.getStudentById(id))
            }
        }


    override suspend fun deleteStudent(student: Student): Job = withContext(Dispatchers.IO) {
        launch {
            studentDao.delete(Mapper.studentToStudentEntity(student))
        }
    }

    override suspend fun addStudent(student: Student): Job = withContext(Dispatchers.IO) {
        launch {
            studentDao.insert(Mapper.studentToStudentEntity(student))
        }
    }


    override suspend fun updateStudent(student: Student): Job = withContext(Dispatchers.IO) {
        launch {
            studentDao.update(Mapper.studentToStudentEntity(student))
        }
    }


}