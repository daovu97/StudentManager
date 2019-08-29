package com.daovu65.studentmanager.ui.activity.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.daovu65.studentmanager.InjectionUtil
import com.daovu65.studentmanager.R
import com.daovu65.studentmanager.databinding.ActivityProfileBinding
import com.daovu65.studentmanager.ui.activity.editProfile.EditProfileActivity
import com.daovu65.studentmanager.ui.activity.main.MainActivity
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {

    companion object {
        const val BUNDLE_EDIT_STUDENT = "BUNDLE_EDIT_STUDENT"
        const val BUNDLE_USER_PROFILE = "BUNDLE_USER_PROFILE"
    }

    private lateinit var viewModel: ProfileViewModel
    lateinit var viewModelFactory: ProfileVMFactory
    private var currentStudentId: Int? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        InjectionUtil.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = viewModelFactory.create(ProfileViewModel::class.java)
        val binding: ActivityProfileBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_profile)

        binding.lifecycleOwner = this
        binding.profileViewmodel = viewModel

        intent.extras?.let { bundle ->
            currentStudentId = bundle.getInt(MainActivity.BUNDLE_STUDENT_ID)
            currentStudentId?.let {
                viewModel.getStudentById(it)
                viewModel.imageProfile.observe(this, Observer { imagePath ->
                    Glide.with(this)
                        .load(imagePath)
                        .error(R.mipmap.ic_launcher_round)
                        .into(image_profile)
                })
            }

        }

        btn_back.setOnClickListener {
            finish()
        }

        btn_edit.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.putExtra(
                MainActivity.BUNDLE_ADD_NEW,
                BUNDLE_EDIT_STUDENT
            )
            intent.putExtra(BUNDLE_USER_PROFILE, currentStudentId)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        currentStudentId?.let {
            viewModel.getStudentById(it)
        }
    }
}
