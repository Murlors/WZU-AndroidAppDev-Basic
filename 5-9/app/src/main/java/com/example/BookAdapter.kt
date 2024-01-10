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

class BookAdapter(context: Context, private val resource: Int, private val books: MutableList<Book>) :
    ArrayAdapter<Book>(context, resource, books) {

    private class ViewHolder(view: View) {
        val tvTitle: TextView = view.findViewById(R.id.row_view_tv_title)
        val tvAuthor: TextView = view.findViewById(R.id.row_view_tv_author)
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

        val book = books[position]
        holder.tvTitle.text = book.title
        holder.tvAuthor.text = book.author
        val imgSrc = book.imgSrc
        Glide.with(context).load(Uri.parse(imgSrc)).into(holder.iv)

        return view!!
    }
}