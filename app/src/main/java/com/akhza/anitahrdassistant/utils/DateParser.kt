package com.akhza.anitahrdassistant.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat

object DateParser {
    @SuppressLint("SimpleDateFormat")
    private fun formatDate(date: String, format: String): String {
        var result = ""

        val oldDate = SimpleDateFormat("dd/MM/yyyy HH:mm")
        try {
            val old = oldDate.parse(date)
            val newFormat = SimpleDateFormat(format)
            result = newFormat.format(old)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return result
    }

    @SuppressLint("SimpleDateFormat")
    private fun formatDateWithoutHour(date: String, format: String): String {
        var result = ""

        val oldDate = SimpleDateFormat("dd/MM/yyyy")
        try {
            val old = oldDate.parse(date)
            val newFormat = SimpleDateFormat(format)
            result = newFormat.format(old)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return result
    }


    fun getLongDate(date: String): String {
        return formatDate(date, "EEEE, dd MMMM yyyy HH:mm")
    }

    fun getLongDateWithoutHour(date: String): String {
        return formatDateWithoutHour(date, "EEEE, dd MMMM yyyy")
    }

    fun getShortDate(date: String): String {
        return formatDate(date, "dd-MM-yyyy")
    }
}