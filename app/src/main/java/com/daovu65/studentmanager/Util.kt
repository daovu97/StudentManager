package com.daovu65.studentmanager

import java.text.DateFormat

object Util {
    fun dateFormat(time: Long): String {
        return DateFormat.getDateInstance().format(time * 1000L)
    }

}