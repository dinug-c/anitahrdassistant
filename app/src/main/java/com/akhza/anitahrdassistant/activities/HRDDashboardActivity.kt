package com.akhza.anitahrdassistant.activities

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.akhza.anitahrdassistant.R
import kotlinx.android.synthetic.main.activity_hrddashboard.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.textColor

class HRDDashboardActivity : AppCompatActivity() {

    var sortBy: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hrddashboard)

        hrddashboard_card_applicants.setBackgroundResource(R.drawable.header_applicants_card)

        hrddashboard_button_sortdate.onClick {
            sortBy = "dateSchedule"
            setSortDate(true)
            setSortPos(false)
        }

        hrddashboard_button_sortpos.onClick {
            sortBy = "position"
            setSortDate(false)
            setSortPos(true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setSortDate(state: Boolean) {
        if(state) {
            hrddashboard_button_sortdate.textColor = Color.WHITE
            hrddashboard_button_sortdate.setBackgroundResource(R.drawable.button_blue_selected)
        } else {
            hrddashboard_button_sortdate.textColor = getColor(R.color.colorBlue)
            hrddashboard_button_sortdate.setBackgroundResource(R.drawable.button_blue_unselected)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setSortPos(state: Boolean) {
        if(state) {
            hrddashboard_button_sortpos.textColor = Color.WHITE
            hrddashboard_button_sortpos.setBackgroundResource(R.drawable.button_blue_selected)
        } else {
            hrddashboard_button_sortpos.textColor = getColor(R.color.colorBlue)
            hrddashboard_button_sortpos.setBackgroundResource(R.drawable.button_blue_unselected)
        }
    }
}