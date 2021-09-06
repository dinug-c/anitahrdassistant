package com.akhza.anitahrdassistant.activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.akhza.anitahrdassistant.R
import com.akhza.anitahrdassistant.VidcallActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_applicants_join_interview.*
import kotlinx.android.synthetic.main.activity_hrdcreate_interview.*
import kotlinx.android.synthetic.main.activity_hrddetail_interview.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class HRDDetailInterviewActivity : AppCompatActivity() {

    lateinit var intentDetail: Intent
    lateinit var roomCode: String
    lateinit var state: String

    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hrddetail_interview)

        intentDetail = intent
        firestore = FirebaseFirestore.getInstance()

        roomCode = intentDetail.getStringExtra("roomCode").toString()
        state = intentDetail.getStringExtra("state").toString()

        if(state == "2") {
            hrddetail_textView_expressionError.text = "No result"
            hrddetail_textView_verbalError.text = "No result"
            hrddetail_button_salary.isGone = false
        } else {
            hrddetail_textView_expressionError.text = "Applicants aren't allowed to see this result"
            hrddetail_textView_verbalError.text = "Applicants aren't allowed to see this result"
            hrddetail_button_salary.isGone = true
        }

        loadInterview(roomCode)

        hrddetail_button_copy.onClick {
            val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("roomCode", hrddetail_textView_roomCode.text)
            clipboardManager.setPrimaryClip(clipData)

            toast("Room Code has copied.")
        }

        hrddetail_button_back.onClick {
            onBackPressed()
        }

        hrddetail_button_start.onClick {
            startActivity<VidcallActivity>()
        }
    }

    private fun loadInterview(roomCode: String) {
        firestore.collection("interviews").document(roomCode)
                .get()
                .addOnCompleteListener { taskId ->
                    if (taskId.isSuccessful) {
                        val getDoc = taskId.result

                        val nameRecruiter = getDoc?.getString("nameRecruiter")
                        val position = getDoc?.getString("position")
                        val starting = getDoc?.getString("isStarting")?.toInt()

                        if(nameRecruiter?.isEmpty()!!) {
                            hrddetail_textView_nameDynamic.text = "Unassigned"
                        } else {
                            hrddetail_textView_nameDynamic.text = nameRecruiter
                        }

                        Log.d("STARTING INT", "${getDoc.getString("isStarting")}")
                        Log.d("STARTING BOOLEAN", "${getDoc.getString("isStarting").toBoolean()}")
                        hrddetail_textView_roomCode.text = roomCode
                        hrddetail_textView_positionDynamic.text = position
                        hrddetail_constraintLayout_ongoing.isVisible = starting == 1

                    }
                }
    }
}