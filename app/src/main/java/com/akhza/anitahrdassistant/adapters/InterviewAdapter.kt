package com.akhza.anitahrdassistant.adapters

import android.content.Context
import android.opengl.Visibility
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.akhza.anitahrdassistant.R
import com.akhza.anitahrdassistant.models.InterviewItems
import com.akhza.anitahrdassistant.utils.DateParser
import kotlinx.android.synthetic.main.items_interview.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class InterviewAdapter(val context: Context, private val listener: (InterviewItems) -> Unit):
RecyclerView.Adapter<InterviewAdapter.ViewHolder>() {

    var listInterview: MutableList<InterviewItems> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.items_interview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(listInterview[position], listener)
    }

    override fun getItemCount(): Int {
        return listInterview.size
    }

    class ViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        fun bindData(data: InterviewItems, listener: (InterviewItems) -> Unit) {
            if(data.nameRecruiter.isNullOrEmpty()) {
                itemView.items_interview_textView_name.text = "Unassigned"
            } else {
                itemView.items_interview_textView_name.text = data.nameRecruiter
            }
            itemView.items_interview_textView_pos.text = data.position
            itemView.items_interview_textView_date.text = "Interview on ${DateParser.getLongDate(data.dateSchedule.toString())}"

            val dateUpdated = data.dateCreated
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            var convertedDate = Date()
            if(!TextUtils.isEmpty(dateUpdated)) {

                try {
                    convertedDate = sdf.parse(dateUpdated)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

                val p = PrettyTime()
                val dateAfter = p.format(convertedDate)

                itemView.items_interview_textView_modified.text = dateAfter
            }

            val sdfNoHour = SimpleDateFormat("dd/MM/yyyy")
            val dateInterview = data.dateSchedule?.split(" ")?.get(0)
            val dateNow = sdfNoHour.format(convertedDate)

            //val dateInterview = DateParser.getShortDate(rawInterview.toString())
            Log.d("DATE INTERVIEW", "$dateInterview")
            Log.d("DATE NOW", "$dateNow")
            if(dateNow.split("/")[1].toInt() >= dateInterview?.split("/")?.get(1)?.toInt()!!) {
                if (dateNow.split("/")[0].toInt() >= dateInterview.split("/")[0].toInt()) {
                    itemView.items_interview_textView_date.isGone = true
                    itemView.items_interview_frameLayout_finished.isGone = false
                } else {
                    itemView.items_interview_textView_date.isGone = false
                    itemView.items_interview_frameLayout_finished.isGone = true
                }
            } else {
                itemView.items_interview_textView_date.isGone = false
                itemView.items_interview_frameLayout_finished.isGone = true
            }

            itemView.onClick {
                listener(data)
            }
        }
    }
}