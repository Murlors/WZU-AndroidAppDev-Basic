package com.example.hlt

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlin.String
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var seekBar: SeekBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var listViewNews: ListView

    private val urlTemplate = "https://tw.wzu.edu.cn/twxw2/%d.htm"

    private var webMaxPage = fetchMaxPageNum("https://tw.wzu.edu.cn/twxw2.htm")
    private val programMaxPage = 20
    private var pageIndex = 0

    fun fetchMaxPageNum(url: String): Int {
        var maxPage = 0
        Thread{
            try {
                val doc = Jsoup.connect(url).get()
                val element = doc.select("div.box_right_main table table td:nth-child(2) a:nth-child(3)")
                maxPage = element.attr("href").split("/")[1].split(".")[0].toInt()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
        while (maxPage == 0) {
            Thread.sleep(100)
        }
        return maxPage
    }

    fun pageChanged() {
        val url = if (pageIndex == 0) {
            "https://tw.wzu.edu.cn/twxw2.htm"
        } else {
            urlTemplate.format(webMaxPage - pageIndex + 1)
        }
        FetchNewsTask(viewModel, url).start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar = findViewById(R.id.seek_bar)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        listViewNews = findViewById(R.id.list_view)

        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        val item = viewModel.news

        swipeRefreshLayout.setOnRefreshListener {
            pageIndex++
            if (pageIndex > programMaxPage) {
                pageIndex = 0
            }
            seekBar.progress = pageIndex
            pageChanged()
            swipeRefreshLayout.isRefreshing = false
        }

        seekBar.max = programMaxPage
        seekBar.progress = pageIndex
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                pageIndex = seekBar?.progress ?: 0
                pageChanged()
            }
        })

        pageChanged()

        listViewNews.setOnItemClickListener { _, _, position, _ ->
            val it = item.value?.get(position)
            val intent = Intent(this, NewsItemActivity::class.java)
            intent.putExtra("item", it)
            startActivity(intent)
        }

        item.observe(this) {
            val adapter = NewsAdapter(this, R.layout.row_view, it)
            listViewNews.adapter = adapter
        }
    }
}