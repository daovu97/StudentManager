package com.daovu65.studentmanager.ui.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.daovu65.studentmanager.InjectionUtil
import com.daovu65.studentmanager.R
import com.daovu65.studentmanager.Util
import com.daovu65.studentmanager.domain.entity.Student
import com.daovu65.studentmanager.ui.viewmodel.EditProfileVMFactory
import com.daovu65.studentmanager.ui.viewmodel.EditProfileViewModel
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.util.*




class EditProfileActivity : AppCompatActivity() {

    companion object {
        val listSex: List<String> = listOf("Men", "Women", "Other")
        val SexValue = listOf(0, 1, 2)
        private const val REQUEST_PICK_IMAGE = 0
    }

    private lateinit var viewModel: EditProfileViewModel
    lateinit var viewModelFactory: EditProfileVMFactory
    private var mState: Int = 0
    private val mBirth: Calendar = Calendar.getInstance()
    private var selectedImageUrl: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        InjectionUtil.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        intent.extras?.let { bundle ->
            when (bundle.get(MainActivity.BUNDLE_ADD_NEW)) {
                MainActivity.BUNDLE_ADD_NEW -> mState = 1
                ProfileActivity.BUNDLE_EDIT_STUDENT -> mState = 2
            }
        }

        viewModel = viewModelFactory.create(EditProfileViewModel::class.java)

        spiner_sex.apply {
            adapter = ArrayAdapter(
                this@EditProfileActivity,
                android.R.layout.simple_dropdown_item_1line,
                listSex
            )
        }

        edt_birth.apply {
            val date = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                mBirth.set(Calendar.YEAR, year)
                mBirth.set(Calendar.MONTH, month)
                mBirth.set(Calendar.DAY_OF_MONTH, day)
                updateBirth(mBirth)
            }

            setOnClickListener {
                DatePickerDialog(
                    this@EditProfileActivity,
                    date,
                    mBirth.get(Calendar.YEAR),
                    mBirth.get(Calendar.MONTH),
                    mBirth.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }

        image_profile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                REQUEST_PICK_IMAGE
            )
        }

        btn_cancel.setOnClickListener {
            finish()
        }

        when (mState) {
            0 -> return
            1 -> addNewStudent()
            2 -> editStudentProfile()
        }

    }

    private fun updateBirth(birth: Calendar) {
        edt_birth.text = Util.dateFormat(birth.timeInMillis / 1000L)
    }

    private fun addNewStudent() {
        btn_delete.visibility = View.GONE
        btn_save.setOnClickListener {
            val firstName = edt_first_name.text.toString()
            val lastName = edt_last_name.text.toString()
            val birth = mBirth.timeInMillis / 1000L
            val sex = SexValue[spiner_sex.selectedItemPosition]
            val address = edt_address.text.toString()
            val major = edt_major.text.toString()
            val imageProfile = selectedImageUrl.toString()
            val student = Student(
                id = 0,
                firstName = firstName,
                lastName = lastName,
                birth = birth,
                sex = sex,
                address = address,
                major = major,
                imageProfile = imageProfile
            )

            viewModel.addStudent(student)

            finish()

        }
    }

    private fun editStudentProfile() {
        var currentStudent: Student? = null
        intent.extras?.getStringArrayList(ProfileActivity.BUNDLE_USER_PROFILE)?.let {
            currentStudent = Student(
                id = Integer.parseInt(it[0]),
                firstName = it[1],
                lastName = it[2],
                birth = it[3].toLong(),
                sex = Integer.parseInt(it[4]),
                address = it[5],
                major = it[6],
                imageProfile = ""
            )
        }

        mBirth.timeInMillis = currentStudent!!.birth * 1000L

        currentStudent?.let {
            edt_first_name.setText(it.firstName)
            edt_last_name.setText(it.lastName)
            edt_birth.text = Util.dateFormat(it.birth)
            edt_address.setText(it.address)
            edt_major.setText(it.major)
            spiner_sex.setSelection(it.sex)
        }

        btn_delete.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                currentStudent?.let {
                    viewModel.deleteStudent(it)
                }
                val intent =
                    Intent(this@EditProfileActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        btn_save.setOnClickListener {
            val firstName = edt_first_name.text.toString()
            val lastName = edt_last_name.text.toString()
            val birth = mBirth.timeInMillis / 1000L
            val sex = SexValue[spiner_sex.selectedItemPosition]
            val address = edt_address.text.toString()
            val major = edt_major.text.toString()
            val imageProfile = ""
            val student = Student(
                id = currentStudent!!.id,
                firstName = firstName,
                lastName = lastName,
                birth = birth,
                sex = sex,
                address = address,
                major = major,
                imageProfile = imageProfile
            )
            currentStudent?.let {
                viewModel.updateStudent(student)
            }

            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {

            selectedImageUrl = data.data
            Glide.with(this)
                .load(selectedImageUrl)
                .into(image_profile)
        }
    }

    
}
