package com.akhza.anitahrdassistant.models

data class InterviewItems(
        //token dari firebase
        var tokenVideo: String? = null,

        //token custom generate dari aplikasi
        var roomToken: String? = null,

        //pembuat jadwal interview
        var emailHrd: String? = null,

        //jadwal interview
        var dateSchedule: String? = null,

        //tanggal jadwal dibuat
        var dateCreated: String? = null,

        //calon pelamar
        var emailRecruiter: String? = null,

        //apakah video call sudah dimulai
        var isStarting: Boolean? = null
)
