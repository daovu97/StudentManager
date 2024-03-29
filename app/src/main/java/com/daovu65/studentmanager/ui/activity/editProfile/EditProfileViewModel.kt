package com.daovu65.studentmanager.ui.activity.editProfile

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.daovu65.studentmanager.domain.entity.Student
import com.daovu65.studentmanager.domain.interactor.AddNewStudent
import com.daovu65.studentmanager.domain.interactor.DeleteStudent
import com.daovu65.studentmanager.domain.interactor.GetStudentById
import com.daovu65.studentmanager.domain.interactor.UpdateStudent
import kotlinx.coroutines.*

class EditProfileViewModel(
    private val addNewStudent: AddNewStudent,
    private val deleteStudent: DeleteStudent,
    private val updateStudent: UpdateStudent,
    private val getStudentById: GetStudentById
) : ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var currentStudent: Student? = null

    private val _liveStudent = MutableLiveData<Student>()
    val liveStudent: LiveData<Student>
        get() = _liveStudent
    private val _state = MutableLiveData<Int>()
    val state: LiveData<Int>
        get() = _state

    val firstName = MutableLiveData<String>()


    val lastName = MutableLiveData<String>()


    private val _birthValue = MutableLiveData<Long>()

    private val _birth = MutableLiveData<String>()
    val birth: LiveData<String>
        get() = _birth

    private val _sex = MutableLiveData<Int>()

    private val _imageProfile = MutableLiveData<String>()

    val address = MutableLiveData<String>()

    val major = MutableLiveData<String>()

    @UiThread
    fun getStudentById(id: Int) {
        uiScope.launch {
            currentStudent = getStudentById.invoke(id)
            _liveStudent.postValue(currentStudent)
            updateDataView(currentStudent)
        }

    }

    fun setState(instate: Int) {
        _state.value = instate
    }

    fun setAgeValue(birth: Long) {
        _birthValue.value = birth
    }

    fun setSexValue(inSex: Int) {
        _sex.value = inSex
    }

    fun setImageProfileValue(path: String) {
        _imageProfile.value = path
    }

    private fun getDataView(): Student? {
        val inputFirstName = firstName.value ?: ""
        if (inputFirstName.isBlank() || inputFirstName.isEmpty()) return null
        val inputLastName = lastName.value ?: ""
        val inputAge = _birthValue.value ?: 0
        val inputAddress = address.value ?: ""
        val inputMajor = major.value ?: ""
        val id = currentStudent?.id ?: 0
        val inputImage = _imageProfile.value ?: ""
        val inputSex = _sex.value ?: 0
        return Student(
            id = id,
            firstName = inputFirstName,
            lastName = inputLastName,
            birth = inputAge,
            sex = inputSex,
            address = inputAddress,
            major = inputMajor,
            imageProfile = inputImage
        )

    }

    private fun updateDataView(student: Student?) {
        student?.let {
            firstName.postValue(it.firstName)
            lastName.postValue(it.lastName)
            _birthValue.postValue(it.birth)
            _birth.postValue(com.daovu65.studentmanager.Util.dateFormat(it.birth))
            address.postValue(it.address)
            major.postValue(it.major)
            _imageProfile.postValue(it.imageProfile)
            _sex.postValue(it.sex)
        }
    }

    @WorkerThread
    fun addStudent(onSuccess: (Boolean) -> Unit) {
        val student = getDataView()
        if (student != null) {
            uiScope.launch {
                addNewStudent.invoke(student)
                onSuccess(true)
            }
        } else onSuccess(false)

    }

    @WorkerThread
    fun deleteStudent(onSuccess: (Boolean) -> Unit) = uiScope.launch {
        if (currentStudent != null) {
            deleteStudent.invoke(currentStudent!!)
            onSuccess(true)
        } else onSuccess(false)
    }

    @WorkerThread
    fun updateStudent(onSuccess: (Boolean) -> Unit) {
        val student = getDataView()
        if (student != null) {
            uiScope.launch {
                updateStudent.invoke(student)
                onSuccess(true)
            }
        } else onSuccess(false)

    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}
