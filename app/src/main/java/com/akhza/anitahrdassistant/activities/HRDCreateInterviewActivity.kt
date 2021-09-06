package com.akhza.anitahrdassistant.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.Intent.createChooser
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.annotation.RequiresApi
import com.akhza.anitahrdassistant.R
import com.akhza.anitahrdassistant.utils.DateParser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_hrdcreate_interview.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class HRDCreateInterviewActivity : AppCompatActivity() {

    lateinit var roomCode: String
    lateinit var firestore: FirebaseFirestore

    val cal = Calendar.getInstance()
    val y = cal.get(Calendar.YEAR)
    val m = cal.get(Calendar.MONTH)
    val d = cal.get(Calendar.DAY_OF_MONTH)
    val hh = cal.get(Calendar.HOUR)
    val mm = cal.get(Calendar.MINUTE)

    lateinit var selectedDate: String
    lateinit var selectedTime: String

    lateinit var intentCreate: Intent
    lateinit var company: String

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hrdcreate_interview)

        intentCreate = intent
        val emailHrd = intentCreate.getStringExtra("email")
        val nameHrd = intentCreate.getStringExtra("name")
        company = intentCreate.getStringExtra("company").toString()

        selectedTime = ""
        selectedDate = ""

        firestore = FirebaseFirestore.getInstance()

        generateRoomCode()

        hrdcreate_button_back.onClick {
            onBackPressed()
        }

        hrdcreate_button_date.onClick {
            val dateSchedule = DatePickerDialog(this@HRDCreateInterviewActivity, DatePickerDialog.OnDateSetListener { datePicker, yyyy, mm, dd ->
                selectedDate = "$dd/${mm+1}/$yyyy"
                Log.d("SELECTED DATE", "$selectedDate")
                hrdcreate_textView_date.text = DateParser.getLongDateWithoutHour(selectedDate)
            }, y, m, d)

            dateSchedule.show()
        }

        hrdcreate_button_time.onClick {
            val timeSchedule = TimePickerDialog(this@HRDCreateInterviewActivity, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                selectedTime = String.format("%02d:%02d", hour, minute)
                Log.d("SELECTED TIME", "$selectedTime")
                hrdcreate_textView_time.text = selectedTime
            }, hh, mm, true)

            timeSchedule.show()
        }

        hrdcreate_button_schedule.onClick {
            val position = hrdcreate_editText_position.text
            if(position.isNullOrBlank()) {
                toast("Position must not be empty")
            }
            if(selectedDate.isNullOrEmpty()) {
                toast("Date schedule must not be empty")
            }
            if(selectedTime.isNullOrEmpty()) {
                toast("Time schedule must not be empty")
            }

            if(!position.isNullOrBlank() && !selectedDate.isNullOrBlank() && !selectedTime.isNullOrBlank()) {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                val dateNow = Date()
                val created = dateFormat.format(dateNow).toString()

                val sendEmail = hrdcreate_checkbox.isChecked
                scheduleInterview(
                        created,
                        "$selectedDate $selectedTime",
                        emailHrd.toString(),
                        nameHrd.toString(),
                        "0",
                        "",
                        "",
                        position.toString(),
                        false,
                        roomCode,
                        "-",
                        sendEmail
                )
            }
        }

        hrdcreate_button_copy.onClick {
            val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("roomCode", hrdcreate_textView_codeDynamic.text)
            clipboardManager.setPrimaryClip(clipData)

            toast("Room Code has copied.")
        }
    }

    private fun scheduleInterview(dateCreated: String, dateSchedule: String, emailHrd: String, nameHrd: String, isStarting: String, emailRecruiter: String, nameRecruiter: String, position: String, recruiterJoined: Boolean, roomToken: String, tokenVideo: String, sendEmail: Boolean) {
        val scheduleMap = hashMapOf(
                "dateCreated" to dateCreated,
                "dateSchedule" to dateSchedule,
                "emailHrd" to emailHrd,
                "nameHrd" to nameHrd,
                "isStarting" to isStarting,
                "emailRecruiter" to emailRecruiter,
                "nameRecruiter" to nameRecruiter,
                "position" to position,
                "recruiterJoined" to recruiterJoined,
                "roomToken" to roomToken,
                "tokenVideo" to tokenVideo,
                "company" to company
        )

        firestore.collection("interviews")
                .document(roomCode)
                .set(scheduleMap)
                .addOnCompleteListener { taskId ->
                    if(taskId.isSuccessful) {
                        if(sendEmail) {
                            alert("Interview scheduled, proceed to sending via Email.", "Scheduled"){
                                positiveButton("SEND EMAIL") {
                                    val emailMessage =
                                            """Dear Applicants,
                                            |Regarding from your applications on our company, we are glad to inform you that yu are our best candidates.
                                            |Our interview schedule will be held on Anita HRD Assistant app, sign up your account and join using our meeting code : $roomToken.
                                            |The interview will be scheduled on ${DateParser.getLongDate(dateSchedule)}. Thank you for your attention.
                                            |
                                            |Regards,
                                            |$nameHrd from $company
                                        """.trimMargin()

                                    val emailSubject = "Interview Invitation on $position"

                                    sendEmail(emailSubject, emailMessage)
                                }
                            }.show()
                        } else {
                            alert("Interview scheduled, make sure to send the Room code to the applicant.", "Scheduled"){
                                positiveButton("OK") {
                                    onBackPressed()
                                }
                            }.show()
                        }
                    }
                }.addOnFailureListener { exception ->
                    exception.printStackTrace()
                }
    }

    private fun sendEmail(subject: String, message: String) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.type = "text/plain"

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, message)

        try {
            startActivity(createChooser(emailIntent, "Choose App"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun generateRoomCode() {
        val randomDigit = java.util.Random()
        val digit1 = randomDigit.nextInt(999)
        val digit2 = randomDigit.nextInt(999)
        val digit3 = randomDigit.nextInt(9999)

        roomCode = "$digit1-$digit2-$digit3"
        hrdcreate_textView_codeDynamic.text = roomCode
    }
}