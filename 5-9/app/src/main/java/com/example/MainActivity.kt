package com.example.hlt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.util.Objects

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: BookViewModel
    private lateinit var seekBar: SeekBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var listViewBooks: ListView

    private val urlTemplate = "http://www.tup.tsinghua.edu.cn/booksCenter/new_book.ashx?pageIndex=%d&pageSize=15&id=0&jcls=0"

    private val MAX_PAGE = 20
    private var pageIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar = findViewById(R.id.seek_bar)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        listViewBooks = findViewById(R.id.list_view)

        viewModel = ViewModelProvider(this)[BookViewModel::class.java]
        val books = viewModel.books

        swipeRefreshLayout.setOnRefreshListener {
            pageIndex ++
            if (pageIndex > MAX_PAGE) {
                pageIndex = 0
            }
            seekBar.progress = pageIndex

            val url = urlTemplate.format(pageIndex)
            FetchBooksTask(viewModel, url).start()
            swipeRefreshLayout.isRefreshing = false
        }

        seekBar.max = MAX_PAGE
        seekBar.progress = pageIndex
        seekBar.setOnSeekBarChangeListener (object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) { }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                pageIndex = seekBar?.progress ?: 0
                val url = urlTemplate.format(pageIndex)
                FetchBooksTask(viewModel, url).start()
            }
        })

        FetchBooksTask(viewModel, urlTemplate.format(pageIndex)).start()

        listViewBooks.setOnItemClickListener { _, _, position, _ ->
            val book = books.value?.get(position)
            val intent = Intent(this, BookItemActivity::class.java)
            intent.putExtra("book", book)
            startActivity(intent)
        }

        books.observe(this) {
            val adapter = BookAdapter(this, R.layout.row_view, it)
            listViewBooks.adapter = adapter
        }

    }
}