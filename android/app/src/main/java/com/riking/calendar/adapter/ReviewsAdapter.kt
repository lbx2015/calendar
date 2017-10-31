package com.riking.calendar.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.riking.calendar.R
import java.util.*


class ReviewsAdapter(private val context: Context) : RecyclerView.Adapter<ReviewViewHolder>() {

    var mList: List<String>

    init {
        mList = ArrayList()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
                R.layout.review_item, viewGroup, false)
        return ReviewViewHolder(view)
    }


    override fun onBindViewHolder(h: ReviewViewHolder, i: Int) {
        val options = RequestOptions()
        Glide.with(context).load(R.drawable.img_user_head)
                .apply(options.fitCenter())
                .into(h.userIcon)
    }


    override fun getItemCount(): Int {
        return mList.size + 5
    }
}
