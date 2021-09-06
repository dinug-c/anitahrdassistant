package com.akhza.anitahrdassistant.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isGone
import com.akhza.anitahrdassistant.R
import com.akhza.anitahrdassistant.utils.DateParser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_applicants_join_interview.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class ApplicantsJoinInterviewActivity : AppCompatActivity() {

    lateinit var firestore: FirebaseFirestore
    lateinit var intentDashboard: Intent
    lateinit var email: String
    lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applicants_join_interview)

        firestore = FirebaseFirestore.getInstance()
        intentDashboard = intent

        email = intentDashboard.getStringExtra("email").toString()
        name = intentDashboard.getStringExtra("name").toString()

        applicantsjoin_button_back.onClick {
            onBackPressed()
        }

        applicantsjoin_button_search.onClick {
            val roomCode = applicantsjoin_editText_code.text
            if(roomCode?.isEmpty()!!) {
                toast("Room code must not be empty.")
            } else {
                searchInterview(roomCode.toString())
            }
        }

        applicantsjoin_button_join.onClick {
            alert("Are you sure you want to join this room's interview?", "Join Room"){
                negativeButton("CANCEL") { }
                positiveButton("OK") {
                    val joinMap = hashMapOf(
                            "emailRecruiter" to email,
                            "nameRecruiter" to name,
                            "recruiterJoined" to true
                    )

                    firestore.collection("interviews")
                            .document(applicantsjoin_editText_code.text.toString())
                            .update(joinMap as Map<String, Any>)
                            .addOnSuccessListener { void ->
                                alert("Success joined this room's interview", "Success"){
                                    positiveButton("OK") { onBackPressed() }
                                }.show()
                            }
                            .addOnFailureListener { exception ->
                                exception.printStackTrace()
                            }
                }
            }.show()
        }
    }

    private fun searchInterview(roomCode: String) {
        applicantsjoin_constraintLayout_loading.isGone = false
        applicantsjoin_constraintLayout_result.isGone = true
        applicantsjoin_constraintLayout_empty.isGone = true
        firestore.collection("interviews")
                .get()
                .addOnCompleteListener { taskId ->
                    if(taskId.isSuccessful) {
                        if (taskId.result!!.size() != 0 && !taskId.result!!.isEmpty) {
                            for(ds in taskId.result!!) {
                                if(ds.getString("roomToken") == roomCode) {
                                    if(!ds.getBoolean("recruiterJoined")!!) {
                                        val companyName = ds.getString("company")
                                        val position = ds.getString("position")
                                        val emailHrd = ds.getString("emailHrd")
                                        val nameHrd = ds.getString("nameHrd")
                                        val dateSchedule = ds.getString("dateSchedule")

                                        applicantsjoin_textView_companyDynamic.text = companyName
                                        applicantsjoin_textView_positionDynamic.text = position
                                        applicantsjoin_textView_hrdDynamic.text = emailHrd
                                        applicantsjoin_textView_nameDynamic.text = nameHrd
                                        applicantsjoin_textView_dateDynamic.text = DateParser.getLongDate(dateSchedule.toString())

                                        applicantsjoin_constraintLayout_loading.isGone = true
                                        applicantsjoin_constraintLayout_empty.isGone = true
                                        applicantsjoin_constraintLayout_result.isGone = false
                                    } else {
                                        applicantsjoin_constraintLayout_loading.isGone = true
                                        applicantsjoin_constraintLayout_result.isGone = true
                                        applicantsjoin_constraintLayout_empty.isGone = false
                                    }
                                } else {
                                    applicantsjoin_constraintLayout_loading.isGone = true
                                    applicantsjoin_constraintLayout_result.isGone = true
                                    applicantsjoin_constraintLayout_empty.isGone = false
                                }
                            }
                        } else {
                            applicantsjoin_constraintLayout_loading.isGone = true
                            applicantsjoin_constraintLayout_result.isGone = true
                            applicantsjoin_constraintLayout_empty.isGone = false
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    exception.printStackTrace()

                    applicantsjoin_constraintLayout_loading.isGone = true
                    applicantsjoin_constraintLayout_result.isGone = true
                    applicantsjoin_constraintLayout_empty.isGone = false
                }
    }
}