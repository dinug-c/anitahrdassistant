package com.akhza.anitahrdassistant.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhza.anitahrdassistant.R
import com.akhza.anitahrdassistant.adapters.InterviewAdapter
import com.akhza.anitahrdassistant.models.InterviewItems
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_hrddashboard.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textColor

class HRDDashboardActivity : AppCompatActivity() {

    var sortBy: String? = ""

    lateinit var db: FirebaseFirestore
    lateinit var adapter: InterviewAdapter
    var interviewList: MutableList<InterviewItems> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hrddashboard)

        db = FirebaseFirestore.getInstance()

        val spUser = getSharedPreferences("Login Info", MODE_PRIVATE)
        val email = spUser?.getString("email", "")
        Log.d("EMAIL DASHBOARD", email.toString())

        adapter = InterviewAdapter(this) {
            startActivity<MainActivity>(
                    "roomToken" to it.roomToken, "tokenVideo" to it.tokenVideo,
                    "emailHrd" to it.emailHrd, "nameHrd" to it.nameHrd,
                    "dateSchedule" to it.dateSchedule, "dateCreated" to it.dateCreated,
                    "emailRecruiter" to it.emailRecruiter, "nameRecruiter" to it.nameRecruiter,
                    "isStarting" to it.isStarting, "position" to it.position
            )
        }

        adapter.listInterview = interviewList
        hrddashboard_recyclerView_content.layoutManager = LinearLayoutManager(this)
        hrddashboard_recyclerView_content.adapter = adapter
        adapter.notifyDataSetChanged()

        loadBasicProfile(email)
        setSortDate(true)

        sortBy = "dateSchedule"
        loadInterviewSchedule(email, sortBy)

        hrddashboard_button_sortdate.onClick {
            interviewList.clear()
            sortBy = "dateSchedule"
            loadInterviewSchedule(email, sortBy)
            hrddashboard_progressBar.isVisible = true
            setSortDate(true)
            setSortPos(false)

            interviewList.sortBy {
                it.dateSchedule
            }
            adapter.notifyDataSetChanged()
        }

        hrddashboard_button_sortpos.onClick {
            interviewList.clear()
            sortBy = "position"
            loadInterviewSchedule(email, sortBy)
            hrddashboard_progressBar.isVisible = true
            setSortDate(false)
            setSortPos(true)

            interviewList.sortBy {
                it.position
            }
            adapter.notifyDataSetChanged()
        }

        hrddashboard_button_profile.onClick {
            startActivity<DetailProfileActivity>("email" to email)
        }

    }

    private fun loadBasicProfile(email: String?) {
        db.collection("accounts").whereEqualTo("email", email)
                .get().addOnCompleteListener { taskId ->
                    if(taskId.isSuccessful) {
                        for (ds in taskId.result!!) {
                            hrddashboard_textView_name.text = ds.getString("name")
                            hrddashboard_textView_company.text = ds.getString("company")
                        }
                    }
                }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadInterviewSchedule(email: String?, sortBy: String?) {
        db.collection("interviews").whereEqualTo("emailHrd", email)
                .get().addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        for (ds in task.result!!) {
                            val interItems = ds.toObject(InterviewItems::class.java)
                            interItems.emailRecruiter = ds.getString("emailRecruiter")
                            interItems.dateSchedule = ds.getString("dateSchedule")
                            interItems.position = ds.getString("position")
                            interItems.tokenVideo = ds.getString("tokenVideo")
                            interItems.roomToken = ds.getString("roomToken")
                            interItems.dateCreated = ds.getString("dateCreated")
                            interItems.emailHrd = ds.getString("emailHrd")
                            interItems.isStarting = ds.getString("isStarting").toBoolean()
                            interItems.nameHrd = ds.getString("nameHrd")
                            interItems.nameRecruiter = ds.getString("nameRecruiter")

                            interviewList.addAll(listOf(interItems))
                            Log.d("INTERVIEW SCHEDULE SIZE", "${interviewList.size}")

                            adapter.listInterview = interviewList
                            adapter.notifyDataSetChanged()
                            hrddashboard_progressBar.isVisible = false
                            hrddashboard_recyclerView_content.isVisible = true

                            Log.d("APPLICANTS", "${interviewList[0].emailRecruiter}")
                        }
                    }
                }.addOnFailureListener { exception ->

                }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setSortDate(state: Boolean) {
        if(state) {
            hrddashboard_button_sortdate.textColor = Color.WHITE
            hrddashboard_button_sortdate.setBackgroundResource(R.drawable.button_blue_selected)
        } else {
            hrddashboard_button_sortdate.textColor = getColor(R.color.colorBlue2)
            hrddashboard_button_sortdate.setBackgroundResource(R.drawable.button_blue_unselected)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setSortPos(state: Boolean) {
        if(state) {
            hrddashboard_button_sortpos.textColor = Color.WHITE
            hrddashboard_button_sortpos.setBackgroundResource(R.drawable.button_blue_selected)
        } else {
            hrddashboard_button_sortpos.textColor = getColor(R.color.colorBlue2)
            hrddashboard_button_sortpos.setBackgroundResource(R.drawable.button_blue_unselected)
        }
    }
}