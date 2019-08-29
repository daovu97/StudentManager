package com.daovu65.studentmanager.ui.activity.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.daovu65.studentmanager.domain.interactor.FindStudentByName
import com.daovu65.studentmanager.domain.interactor.GetAllStudent
import com.daovu65.studentmanager.domain.interactor.SortedBy

class MainVMFactory(
    private val getAllStudent: GetAllStudent,
    private val findStudentByName: FindStudentByName,
    private val sortedBy: SortedBy
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(
            getAllStudent = getAllStudent,
            findStudentByName = findStudentByName,
            sortedBy = sortedBy
        ) as T
    }

}