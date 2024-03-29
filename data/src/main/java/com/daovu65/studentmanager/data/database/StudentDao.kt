package com.daovu65.studentmanager.data.database

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface StudentDao {
    @Query("SELECT * FROM student_table")
    fun getAllStudent(): List<StudentEntity>

    @Query("SELECT * FROM student_table where id =:id")
    fun getStudentById(id: Int): StudentEntity

    @Query("SELECT * FROM student_table WHERE first_name " +
            "LIKE '%' || :search || '%' OR last_name LIKE '%' || :search || '%' OR " +
            "last_name || ' ' || first_name LIKE '%' || :search || '%'")
    fun findUserWithName(search: String): List<StudentEntity>

    @Insert
    fun insert(studentEntity: StudentEntity)

    @Update
    fun update(studentEntity: StudentEntity)

    @Delete
    fun delete(studentEntity: StudentEntity)

    //    @Query("SELECT * FROM student_table ORDER BY :value")
    @RawQuery
    fun sortedBy(sortQuery: SupportSQLiteQuery): List<StudentEntity>
}