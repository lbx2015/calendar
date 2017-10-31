package com.riking.calendar.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.riking.calendar.R
import java.util.*


class ReviewsAdapter(private val context: Context) : RecyclerView.Adapter<ReviewViewHolder>() {

    var mList: List<String>

    init {
        mList = ArrayList()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
                R.layout.question_item, viewGroup, false)
        return ReviewViewHolder(view)
    }


    override fun onBindViewHolder(h: ReviewViewHolder, i: Int) {
    }


    override fun getItemCount(): Int {
        return mList.size + 5
    }
}
