package com.example.hlt

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

class NewsItemActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var settings: WebSettings
    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_item)

        webView = findViewById(R.id.webview)
        settings = webView.settings
        actionBar = supportActionBar!!

        settings.javaScriptEnabled = true
        settings.builtInZoomControls = true
        settings.useWideViewPort = true

        val news = intent.getSerializableExtra("item") as News

        actionBar.title = news.title
        actionBar.subtitle = news.date

        webView.webViewClient = WebViewClient()
        webView.loadUrl(news.href)
    }
}