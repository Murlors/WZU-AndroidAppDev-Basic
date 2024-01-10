package com.example.hlt

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private lateinit var buttonGetData: Button
    private lateinit var textViewBooks: TextView
    private lateinit var viewModel: BookViewModel

    private val url = "http://www.tup.tsinghua.edu.cn/booksCenter/new_book.ashx?pageIndex=0&pageSize=15&id=0&jcls=0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonGetData = findViewById(R.id.btn_get_data)
        textViewBooks = findViewById(R.id.tv_data)
        viewModel = ViewModelProvider(this)[BookViewModel::class.java]
        val books = viewModel.books
        books.observe(this) {
            textViewBooks.text = printBookList(it)
        }

        buttonGetData.setOnClickListener {
            FetchBooksTask(viewModel, url).start()
        }
    }

    private fun printBookList(books: MutableList<Book>): String {
        val bookListString = StringBuilder()
        for ((index, book) in books.withIndex()) {
            bookListString.append("---------------------$index----------------------\n")
            bookListString.append(book.toString())
            bookListString.append("---------------------------------------------\n")
        }
        return bookListString.toString()
    }
}