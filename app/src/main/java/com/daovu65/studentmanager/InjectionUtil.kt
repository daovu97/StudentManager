package com.daovu65.studentmanager

import android.annotation.SuppressLint
import android.content.Context
import com.daovu65.studentmanager.data.StudentRepositoryImpl
import com.daovu65.studentmanager.data.database.StudentDao
import com.daovu65.studentmanager.data.database.StudentDatabase
import com.daovu65.studentmanager.domain.interactor.*
import com.daovu65.studentmanager.domain.repository.StudentRepository
import com.daovu65.studentmanager.ui.activity.editProfile.EditProfileActivity
import com.daovu65.studentmanager.ui.activity.editProfile.EditProfileVMFactory
import com.daovu65.studentmanager.ui.activity.main.MainActivity
import com.daovu65.studentmanager.ui.activity.main.MainVMFactory
import com.daovu65.studentmanager.ui.activity.profile.ProfileActivity
import com.daovu65.studentmanager.ui.activity.profile.ProfileVMFactory

@SuppressLint("StaticFieldLeak")
object InjectionUtil {
    private lateinit var context: Context

    private val database: StudentDatabase by lazy {
        StudentDatabase.getDatabase(context)
    }

    private val dao: StudentDao by lazy {
        database.studentDao
    }

    private val repository: StudentRepository by lazy {
        com.daovu65.studentmanager.data.StudentRepositoryImpl(dao)
    }

    private val getAllStudent: GetAllStudent by lazy {
        GetAllStudent(repository)
    }

    private val getStudentById: GetStudentById by lazy {
        GetStudentById(repository)
    }

    private val addNewStudent: AddNewStudent by lazy {
        AddNewStudent(repository)
    }

    private val deleteStudent: DeleteStudent by lazy {
        DeleteStudent(repository)
    }

    private val updateStudent: UpdateStudent by lazy {
        UpdateStudent(repository)
    }

    private val findStudentByName: FindStudentByName by lazy {
        FindStudentByName(repository)
    }

    private val sortedBy: SortedBy by lazy {
        SortedBy(repository)
    }

    fun inject(mainActivity: MainActivity) {
        this.context = mainActivity.applicationContext
        val viewModelFactory = MainVMFactory(
            getAllStudent = getAllStudent,
            findStudentByName = findStudentByName,
            sortedBy = sortedBy
        )

        mainActivity.viewModelFactory = viewModelFactory
    }

    fun inject(activity: EditProfileActivity) {
        this.context = activity.applicationContext
        val viewModelFactory =
            EditProfileVMFactory(
                addNewStudent = addNewStudent,
                deleteStudent = deleteStudent,
                updateStudent = updateStudent,
                getStudentById = getStudentById
            )

        activity.viewModelFactory = viewModelFactory
    }

    fun inject(profileActivity: ProfileActivity) {
        this.context = profileActivity.applicationContext
        val viewModelFactory = ProfileVMFactory(
            getStudentById = getStudentById
        )

        profileActivity.viewModelFactory = viewModelFactory
    }
}