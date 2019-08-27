package com.daovu65.studentmanager.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.daovu65.studentmanager.domain.entity.Student
import com.daovu65.studentmanager.domain.interactor.FindStudentByName
import com.daovu65.studentmanager.domain.interactor.GetAllStudent
import com.daovu65.studentmanager.domain.interactor.GetStudentById
import kotlinx.coroutines.*

class MainViewModel(
    private val getAllStudent: GetAllStudent,
    private val findStudentByName: FindStudentByName
) : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _listStudent: MutableLiveData<List<Student>> = MutableLiveData()

    val listStudent: LiveData<List<Student>>
        get() = _listStudent


    /*
        sorted by
        first name : 1,
        last name : 2
        birth : 3,
        default : 0
     */
    fun findStudentByName(name: String) {
        uiScope.launch {
            val value = findStudentByName.invoke(name)
            _listStudent.postValue(value)
        }
    }

    fun refreshData(sortedBy: Int) {
        uiScope.launch {
            val value = getAllStudent.invoke()
            when (sortedBy) {
                0 -> _listStudent.postValue(value)
                1
                -> _listStudent.postValue(value.sortedBy {
                    it.firstName
                })
                2 -> _listStudent.postValue(value.sortedBy {
                    it.lastName
                })
                3 -> _listStudent.postValue(value.sortedBy {
                    it.birth
                })
                else -> _listStudent.postValue(value)
            }

        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}