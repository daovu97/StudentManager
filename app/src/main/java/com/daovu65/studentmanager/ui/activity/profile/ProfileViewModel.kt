package com.daovu65.studentmanager.ui.activity.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.daovu65.studentmanager.domain.interactor.GetStudentById
import com.daovu65.studentmanager.ui.activity.editProfile.EditProfileActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getStudentById: GetStudentById
) : ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val sex = MutableLiveData<String>()

    val fullName = MutableLiveData<String>()

    val age = MutableLiveData<String>()

    val imageProfile = MutableLiveData<String>()

    val address = MutableLiveData<String>()

    val major = MutableLiveData<String>()

    fun getStudentById(id: Int) {
        uiScope.launch {
            val student = getStudentById.invoke(id)
            fullName.postValue("${student.lastName} ${student.firstName}")
            age.postValue(com.daovu65.studentmanager.Util.dateFormat(student.birth))
            address.postValue(student.address)
            major.postValue(student.major)
            imageProfile.postValue(student.imageProfile)
            when (student.sex) {
                EditProfileActivity.SexValue[0] -> sex.postValue(
                    EditProfileActivity.listSex[0])
                EditProfileActivity.SexValue[1] -> sex.postValue(
                    EditProfileActivity.listSex[1])
                EditProfileActivity.SexValue[2] -> sex.postValue(
                    EditProfileActivity.listSex[2])
            }
        }

    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}