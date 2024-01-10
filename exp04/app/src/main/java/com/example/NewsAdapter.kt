package com.example.hlt

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class NewsAdapter(
    context: Context,
    private val resource: Int,
    private val news: MutableList<News>
) :
    ArrayAdapter<News>(context, resource, news) {

    private class ViewHolder(view: View) {
        val tvTitle: TextView = view.findViewById(R.id.row_view_tv_title)
        val tvDate: TextView = view.findViewById(R.id.row_view_tv_date)
        val iv: ImageView = view.findViewById(R.id.row_view_iv)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val item = news[position]
        holder.tvTitle.text = item.title
        holder.tvDate.text = item.date
        val imgSrc = item.imgSrc
        Glide.with(context).load(Uri.parse(imgSrc)).centerInside().into(holder.iv)

        return view!!
    }
}