package com.daovu65.studentmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.daovu65.studentmanager.domain.interactor.AddNewStudent
import com.daovu65.studentmanager.domain.interactor.DeleteStudent
import com.daovu65.studentmanager.domain.interactor.UpdateStudent

class EditProfileVMFactory(
    private val addNewStudent: AddNewStudent,
    private val deleteStudent: DeleteStudent,
    private val updateStudent: UpdateStudent
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditProfileViewModel(
            addNewStudent = addNewStudent,
            deleteStudent = deleteStudent,
            updateStudent = updateStudent
        ) as T
    }

}