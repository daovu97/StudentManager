package com.daovu65.studentmanager.ui.activity.editProfile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.daovu65.studentmanager.GrantPermission
import com.daovu65.studentmanager.InjectionUtil
import com.daovu65.studentmanager.R
import com.daovu65.studentmanager.Util
import com.daovu65.studentmanager.databinding.ActivityEditProfileBinding
import com.daovu65.studentmanager.ui.activity.main.MainActivity
import com.daovu65.studentmanager.ui.activity.profile.ProfileActivity
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
        viewModel = viewModelFactory.create(EditProfileViewModel::class.java)
        val binding: ActivityEditProfileBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        intent.extras?.let { bundle ->
            when (bundle.get(MainActivity.BUNDLE_ADD_NEW)) {
                MainActivity.BUNDLE_ADD_NEW -> viewModel.setState(1)
                ProfileActivity.BUNDLE_EDIT_STUDENT -> viewModel.setState(2)
            }
        }

        spiner_sex.apply {
            adapter = ArrayAdapter(
                this@EditProfileActivity,
                android.R.layout.simple_spinner_dropdown_item,
                listSex
            )
        }

        edt_birth.apply {
            val date = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                mBirth.set(Calendar.YEAR, year)
                mBirth.set(Calendar.MONTH, month)
                mBirth.set(Calendar.DAY_OF_MONTH, day)
                edt_birth.setText(Util.dateFormat(mBirth.timeInMillis / 1000L))
                print(mBirth.toString())
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
            val grantPermission = GrantPermission(this)
            if (grantPermission.checkPermission()) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    REQUEST_PICK_IMAGE
                )
            } else grantPermission.requestPermission()

        }

        btn_cancel.setOnClickListener {
            finish()
        }

        viewModel.state.observe(this, androidx.lifecycle.Observer {
            when (it) {
                1 -> addNewStudent()
                2 -> editStudentProfile()
            }
        })

    }

    private fun addNewStudent() {
        btn_delete.visibility = View.GONE
        btn_save.setOnClickListener {
            viewModel.setSexValue(spiner_sex.selectedItemPosition)
            viewModel.setAgeValue((mBirth.timeInMillis / 1000L))
            viewModel.setImageProfileValue(selectedImageUrl.toString())
            viewModel.addStudent {
                if (it) finish()
                else Toast.makeText(this, "Save fail !!, please check again", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun editStudentProfile() {
        intent.extras?.let {
            viewModel.getStudentById(it.getInt(ProfileActivity.BUNDLE_USER_PROFILE))
        }

        viewModel.liveStudent.observe(this, androidx.lifecycle.Observer {
            mBirth.timeInMillis = it.birth * 1000L
            spiner_sex.setSelection(it.sex)
            selectedImageUrl = Uri.parse(it.imageProfile)
            Glide.with(this)
                .load(selectedImageUrl)
                .error(android.R.drawable.ic_menu_add)
                .into(image_profile)
        })

        btn_delete.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                viewModel.deleteStudent {
                    if (it) {
                        val intent = Intent(this@EditProfileActivity, MainActivity::class.java)
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Deleted student!!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@EditProfileActivity, "delete fail !!, please check again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }


            }
        }

        btn_save.setOnClickListener {
            viewModel.setSexValue(spiner_sex.selectedItemPosition)
            viewModel.setAgeValue((mBirth.timeInMillis / 1000L))
            viewModel.setImageProfileValue(selectedImageUrl.toString())
            viewModel.updateStudent {
                if (it) finish()
                else {
                    Toast.makeText(this, "Save fail !!, please check again", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {

            selectedImageUrl = data.data!!
            Glide.with(this)
                .load(selectedImageUrl)
                .into(image_profile)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            GrantPermission.PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    REQUEST_PICK_IMAGE
                )
            }
        }

    }

}
