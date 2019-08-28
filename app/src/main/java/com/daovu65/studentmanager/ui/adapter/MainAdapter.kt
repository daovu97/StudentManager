package com.daovu65.studentmanager.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daovu65.studentmanager.R
import com.daovu65.studentmanager.Util
import com.daovu65.studentmanager.domain.entity.Student
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.File

class MainAdapter(
    private val context: Context,
    private val itemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private var listStudent: List<Student> = emptyList()

    fun submitValue(value: List<Student>) {
        listStudent = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_student, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listStudent.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentStudent = listStudent[position]
        holder.bind(currentStudent)
    }

    fun getStudentAt(position: Int): Student {
        return listStudent[position]
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageProfile: CircleImageView = itemView.findViewById(R.id.image_profile)
        private val name: TextView = itemView.findViewById(R.id.tv_full_name)
        private val birth: TextView = itemView.findViewById(R.id.tv_birth)

        init {
            itemView.setOnClickListener {
                itemClickListener(adapterPosition)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(student: Student) {
            Glide.with(context)
                .load(Uri.parse(student.imageProfile))
                .override(60, 60)
                .error(R.mipmap.ic_launcher_round)
                .into(imageProfile)
            name.text = "${student.lastName} ${student.firstName}"
            birth.text = Util.dateFormat(student.birth)
        }
    }
}