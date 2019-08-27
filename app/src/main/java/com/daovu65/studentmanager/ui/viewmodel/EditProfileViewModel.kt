package com.daovu65.studentmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.daovu65.studentmanager.domain.entity.Student
import com.daovu65.studentmanager.domain.interactor.AddNewStudent
import com.daovu65.studentmanager.domain.interactor.DeleteStudent
import com.daovu65.studentmanager.domain.interactor.UpdateStudent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val addNewStudent: AddNewStudent,
    private val deleteStudent: DeleteStudent,
    private val updateStudent: UpdateStudent
) : ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    fun addStudent(student: Student) {
        uiScope.launch {
            addNewStudent.invoke(student)
        }
    }

    fun deleteStudent(student: Student) {
        uiScope.launch {
            deleteStudent.invoke(student)
        }
    }

    fun updateStudent(student: Student) {
        uiScope.launch {
            updateStudent.invoke(student)
        }
    }


}