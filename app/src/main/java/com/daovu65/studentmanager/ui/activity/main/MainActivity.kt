package com.daovu65.studentmanager.ui.activity.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.daovu65.studentmanager.InjectionUtil
import com.daovu65.studentmanager.R
import com.daovu65.studentmanager.domain.entity.Student
import com.daovu65.studentmanager.ui.activity.editProfile.EditProfileActivity
import com.daovu65.studentmanager.ui.activity.profile.ProfileActivity
import com.daovu65.studentmanager.ui.adapter.MainAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val BUNDLE_ADD_NEW = "BUNDLE_ADD_NEW"
        const val BUNDLE_STUDENT_ID = "BUNDLE_STUDENT_ID"

        val listSortedBy: List<String> =
            listOf(
                "None",
                "First name increase",
                "First name decrease",
                "Last name increase",
                "Age increase",
                "Age decrease"
            )
        val SortedValue =
            listOf(
                null,
                "first_name ASC",
                "first_name DESC",
                "last_name ASC",
                "birth DESC",
                "birth ASC"
            )

    }

    private lateinit var viewModel: MainViewModel
    private lateinit var mAdapter: MainAdapter
    lateinit var viewModelFactory: MainVMFactory
    private var sortedBy: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        InjectionUtil.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = viewModelFactory.create(MainViewModel::class.java)
        initView()
        viewModel.listStudent.observe(this@MainActivity,
            Observer<List<Student>> { t ->
                mAdapter.submitValue(t)
                print(t.toString())
            })
        searchData()
        sortedData()
        swipeToRefresh()
        btn_add_new.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.putExtra(BUNDLE_ADD_NEW, BUNDLE_ADD_NEW)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()
        viewModel.sortedStudent(SortedValue[sortedBy])
        spinner_sortby.setSelection(sortedBy)
    }

    private fun initView() {
        recycler_student.apply {
            mAdapter = MainAdapter(this@MainActivity) {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                intent.putExtra(BUNDLE_STUDENT_ID, mAdapter.getStudentAt(it).id)
                startActivity(intent)
            }

            layoutManager =
                LinearLayoutManager(this@MainActivity)

            adapter = mAdapter

        }
        spinner_sortby.apply {
            adapter = ArrayAdapter(
                this@MainActivity,
                android.R.layout.simple_spinner_dropdown_item,
                listSortedBy
            )

        }
    }

    private fun searchData() {
        edt_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    when {
                        it.isNotEmpty() || it.isNotBlank() -> viewModel.findStudentByName(p0.toString())
                        else -> viewModel.sortedStudent(SortedValue[sortedBy])
                    }

                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })

    }

    private fun sortedData() {
        spinner_sortby.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                sortedBy = p2
                viewModel.sortedStudent(SortedValue[sortedBy])
            }

        }
    }

    private fun swipeToRefresh() {
        swiperefresh.setOnRefreshListener {
            GlobalScope.launch(Dispatchers.IO) {
                viewModel.sortedStudent(SortedValue[sortedBy])
                delay(1000L)
                withContext(Dispatchers.Main) {
                    swiperefresh.isRefreshing = false
                }
            }

        }
    }

}
