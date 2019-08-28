package com.daovu65.studentmanager.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.daovu65.studentmanager.domain.entity.Student
import com.daovu65.studentmanager.domain.interactor.FindStudentByName
import com.daovu65.studentmanager.domain.interactor.GetAllStudent
import com.daovu65.studentmanager.domain.interactor.SortedBy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MainViewModel(
    private val getAllStudent: GetAllStudent,
    private val findStudentByName: FindStudentByName,
    private val sortedBy: SortedBy
) : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _listStudent: MutableLiveData<List<Student>> = MutableLiveData()

    val listStudent: LiveData<List<Student>>
        get() = _listStudent

    fun sortedStudent(value: String) {
        uiScope.launch {
            val value = sortedBy.invoke(value)
            _listStudent.postValue(value)
        }

    }

    fun findStudentByName(name: String) {
        uiScope.launch {
            val value = findStudentByName.invoke(name)
            _listStudent.postValue(value)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}