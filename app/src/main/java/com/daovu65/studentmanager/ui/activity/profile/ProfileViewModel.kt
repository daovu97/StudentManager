package com.daovu65.studentmanager.ui.activity.profile

import androidx.lifecycle.LiveData
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

    private val _sex = MutableLiveData<String>()
    val sex: LiveData<String>
        get() = _sex

    private val _fullName = MutableLiveData<String>()
    val fullName: LiveData<String>
        get() = _fullName

    private val _age = MutableLiveData<String>()
    val age: LiveData<String>
        get() = _age

    private val _imageProfile = MutableLiveData<String>()
    val imageProfile: LiveData<String>
        get() = _imageProfile

    private val _address = MutableLiveData<String>()
    val address: LiveData<String>
        get() = _address

    private val _major = MutableLiveData<String>()
    val major: LiveData<String>
        get() = _major

    fun getStudentById(id: Int) {
        uiScope.launch {
            val student = getStudentById.invoke(id)
            _fullName.postValue("${student.lastName} ${student.firstName}")
            _age.postValue(com.daovu65.studentmanager.Util.dateFormat(student.birth))
            _address.postValue(student.address)
            _major.postValue(student.major)
            _imageProfile.postValue(student.imageProfile)
            when (student.sex) {
                EditProfileActivity.SexValue[0] -> _sex.postValue(
                    EditProfileActivity.listSex[0]
                )
                EditProfileActivity.SexValue[1] -> _sex.postValue(
                    EditProfileActivity.listSex[1]
                )
                EditProfileActivity.SexValue[2] -> _sex.postValue(
                    EditProfileActivity.listSex[2]
                )
            }
        }

    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}