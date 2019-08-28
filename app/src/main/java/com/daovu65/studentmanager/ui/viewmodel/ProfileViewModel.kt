package com.daovu65.studentmanager.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.daovu65.studentmanager.domain.entity.Student
import com.daovu65.studentmanager.domain.interactor.GetStudentById
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getStudentById: GetStudentById
) : ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _liveStudent = MutableLiveData<Student>()
    val liveStudent: LiveData<Student>
        get() = _liveStudent

    fun getStudentById(id: Int) {
        uiScope.launch {
            _liveStudent.postValue(getStudentById.invoke(id))
        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}