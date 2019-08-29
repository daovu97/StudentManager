package com.daovu65.studentmanager.ui.activity.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.daovu65.studentmanager.domain.interactor.GetStudentById

class ProfileVMFactory(
    private val getStudentById: GetStudentById
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(
            getStudentById = getStudentById
        ) as T
    }

}