package com.akhza.anitahrdassistant.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.akhza.anitahrdassistant.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail_profile.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class DetailProfileActivity : AppCompatActivity() {

    private lateinit var intentDetailProfile: Intent
    lateinit var emailProfile: String

    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_profile)

        intentDetailProfile = intent
        emailProfile = intentDetailProfile.getStringExtra("email").toString()
        firestore = FirebaseFirestore.getInstance()

        loadProfile(emailProfile)
        loadInterviews(emailProfile)

        detailprofile_button_back.onClick {
            onBackPressed()
        }
    }

    fun loadProfile(email: String?) {
        detailProfile_constraintLayout_basecontent.isVisible = false
        detailProfile_progressBar_base.isVisible = true
        firestore.collection("accounts").whereEqualTo("email", email)
                .get()
                .addOnCompleteListener { taskId ->
                    if(taskId.isSuccessful) {
                        for (ds in taskId.result!!) {
                            val state = ds.getString("state")
                            val nameGet = ds.getString("name")
                            val emailGet = ds.getString("email")
                            val companyGet = ds.getString("company")
                            val discplineGet = ds.getString("discipline")
                            var roleGet: String = ""

                            when(state) {
                                "1" -> {
                                    roleGet = "Super Admin"
                                    detailprofile_cardView_company.isGone = false
                                }
                                "2" -> {
                                    roleGet = "HRD/Recruiter"
                                    loadInterviews(emailGet)
                                    detailprofile_cardView_company.isGone = false
                                }
                                "3" -> {
                                    roleGet = "Applicants"
                                    detailprofile_cardView_company.isGone = true
                                }
                            }

                            detailprofile_textView_nameDynamic.text = nameGet
                            detailprofile_textView_emailDynamic.text = emailGet
                            detailprofile_textView_roleDynamic.text = roleGet
                            detailprofile_textView_companyDynamic.text = companyGet
                            detailprofile_textView_disciplineDynamic.text = discplineGet

                            detailProfile_constraintLayout_basecontent.isVisible = true
                            detailProfile_progressBar_base.isVisible = false
                        }
                    }
                }
    }

    fun loadInterviews(email: String?) {
        var applicantsJoined: Int = 0
        detailProfile_constraintLayout_companyContent.isVisible = false
        detailProfile_progressBar_company.isVisible = true
        firestore.collection("interviews").whereEqualTo("emailHrd", email)
                .get()
                .addOnCompleteListener { taskId ->
                    if (taskId.isSuccessful) {
                        for (ds in taskId.result!!) {

                            var isJoined = ds.getBoolean("recruiterJoined")

                            if(isJoined!!) {
                                applicantsJoined++
                            }

                        }
                        var interviewCreated: Int = taskId.result!!.size()
                        Log.d("INTERVIEW CREATED", "${taskId.result!!.size()}")
                        Log.d("APPLICANTS JOINED", "$applicantsJoined")
                        detailprofile_textView_createdDynamic.text = interviewCreated.toString()
                        detailprofile_textView_applicantsDynamic.text = applicantsJoined.toString()

                        detailProfile_constraintLayout_companyContent.isVisible = true
                        detailProfile_progressBar_company.isVisible = false
                    }
                }
    }
}