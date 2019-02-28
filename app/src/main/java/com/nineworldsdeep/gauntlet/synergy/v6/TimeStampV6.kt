package com.nineworldsdeep.gauntlet.synergy.v6

import java.text.SimpleDateFormat
import java.util.*

object TimeStampV6 {

    fun yyyy_MM_dd_hh_mm_ss(): String {

        return to_yyyy_MM_dd_hh_mm_ss(Date())
    }

    fun to_yyyy_MM_dd_hh_mm_ss(date: Date?): String {

        if (date == null) {
            return ""
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.US)

        return sdf.format(date)
    }

    fun to_UTC_Yyyy_MM_dd_hh_mm_ss(date: Date?): String {

        if (date == null) {
            return ""
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        sdf.timeZone = TimeZone.getTimeZone("UTC")

        return sdf.format(date)
    }

    fun yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
            yyyy_MM_dd_hh_mm_ss_UTC: String): Date? {

        if (yyyy_MM_dd_hh_mm_ss_UTC.isBlank()) {
            return null
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        sdf.timeZone = TimeZone.getTimeZone("UTC")

        return sdf.parse(yyyy_MM_dd_hh_mm_ss_UTC)
    }

    fun now(): Date {

        return Date()
    }

    fun nowString(): String {

        return to_UTC_Yyyy_MM_dd_hh_mm_ss(now())
    }
}