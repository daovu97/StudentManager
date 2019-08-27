package com.daovu65.studentmanager

import java.util.*
import java.text.SimpleDateFormat


object Util {
    fun dateFormat(time: Long): String {
        val formatter = SimpleDateFormat("dd/mm/yyyy", Locale.US)
        return formatter.format(time * 1000L).toString()
    }

}