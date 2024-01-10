package com.example.hlt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private lateinit var buttonGetData: Button
    private lateinit var listViewBooks: ListView
    private lateinit var viewModel: BookViewModel

    private val url = "http://www.tup.tsinghua.edu.cn/booksCenter/new_book.ashx?pageIndex=0&pageSize=15&id=0&jcls=0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonGetData = findViewById(R.id.btn_get_data)
        listViewBooks = findViewById(R.id.lv_books)
        viewModel = ViewModelProvider(this)[BookViewModel::class.java]
        val books = viewModel.books

        listViewBooks.setOnItemClickListener { _, _, position, _ ->
            val book = books.value?.get(position)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(book?.href))
            startActivity(intent)
        }

        books.observe(this) {
            val adapter = BookAdapter(this, R.layout.row_view, it)
            listViewBooks.adapter = adapter
        }

        buttonGetData.setOnClickListener {
            FetchBooksTask(viewModel, url).start()
        }
    }
}