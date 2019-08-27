package com.daovu65.studentmanager.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.daovu65.studentmanager.InjectionUtil
import com.daovu65.studentmanager.R
import com.daovu65.studentmanager.Util
import com.daovu65.studentmanager.domain.entity.Student
import com.daovu65.studentmanager.ui.viewmodel.ProfileVMFactory
import com.daovu65.studentmanager.ui.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.image_profile
import java.io.File

class ProfileActivity : AppCompatActivity() {

    companion object {
        const val BUNDLE_EDIT_STUDENT = "BUNDLE_EDIT_STUDENT"
        const val BUNDLE_USER_PROFILE = "BUNDLE_USER_PROFILE"
    }

    private lateinit var viewModel: ProfileViewModel
    lateinit var viewModelFactory: ProfileVMFactory
    private var currentStudent: Student? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        InjectionUtil.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        viewModel = viewModelFactory.create(ProfileViewModel::class.java)

        intent.extras?.let { bundle ->
            val id = bundle.get(MainActivity.BUNDLE_STUDENT_ID).toString()
            viewModel.getStudentById(Integer.parseInt(id))
        }

        viewModel.liveStudent.observe(this, Observer {
            currentStudent = it
            updateView(currentStudent)

        })



        btn_back.setOnClickListener {
            finish()
        }

        btn_edit.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.putExtra(MainActivity.BUNDLE_ADD_NEW, BUNDLE_EDIT_STUDENT)
            intent.putStringArrayListExtra(BUNDLE_USER_PROFILE, putExtraToEdit(currentStudent))

            startActivity(intent)
        }
    }

    private fun updateView(student: Student?) {
        student?.let {
            tv_full_name.text = "${it.lastName} ${it.firstName}"
            tv_address.text = it.address
            tv_birth.text = Util.dateFormat(it.birth)
            tv_sex.text = EditProfileActivity.listSex[it.sex]
            tv_major.text = it.major
            Glide.with(this)
                .load(Uri.parse(it.imageProfile))
                .into(image_profile)
        }

    }

    private fun putExtraToEdit(student: Student?): ArrayList<String?> {
        val id = student?.id.toString()
        val firstName = student?.firstName
        val lastName = student?.lastName
        val birth = student?.birth.toString()
        val sex = student?.sex.toString()
        val address = student?.address
        val major = student?.major
        val imageProfile = student?.imageProfile
        return arrayListOf(
            id, firstName, lastName, birth, sex, address, major, imageProfile
        )
    }

    override fun onResume() {
        super.onResume()
        currentStudent?.id?.let {
            viewModel.getStudentById(it)
        }
    }
}
