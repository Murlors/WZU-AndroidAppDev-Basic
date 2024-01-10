package com.example.hlt

import org.jsoup.Jsoup

class FetchNewsTask(private val viewModel: NewsViewModel, private val url: String) : Thread() {
    private val Base_URL = "https://tw.wzu.edu.cn/"
    override fun run() {
        try {
            val news = viewModel.news
            val list = news.value ?: mutableListOf()
            list.clear()
            val doc = Jsoup.connect(url).get()
            val elements = doc.select(".box_right_main ul li")
            for (element in elements) {
                val title = element.select("a").text()
                val date = element.select("samp").text()
                val href = Base_URL + element.select("a").attr("href")
                val imgSrc = getNewsImgSrc(href)
                val item = News(title, date, href, imgSrc)

                list.add(item)
            }
            news.postValue(list)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getNewsImgSrc(url: String): String {
        try {
            val doc = Jsoup.connect(url).get()
            val elements = doc.select("#vsb_content")
            for (element in elements) {
                return Base_URL + element.select("img").attr("src")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}