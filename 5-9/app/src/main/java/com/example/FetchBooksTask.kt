package com.example.hlt

import org.jsoup.Jsoup

class FetchBooksTask(private val viewModel: BookViewModel, private val url: String): Thread() {
    private val Base_URL = "http://www.tup.tsinghua.edu.cn/booksCenter/"

    override fun run() {
        try {
            val books = viewModel.books
            val list = books.value ?: mutableListOf()
            list.clear()
            val doc = Jsoup.connect(url).get()
            val elements = doc.select("dl[class*=product]")
            for (element in elements) {
                val title = element.select("span").first()?.attr("title").toString()
                val author = element.select("p").first()?.text().toString()
                val href = Base_URL + element.select("a").first()?.attr("href")
                val imgSrc = element.select("img").first()?.attr("abs:src").toString()
                list.add(Book(title, author, href, imgSrc))
            }
            books.postValue(list)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}