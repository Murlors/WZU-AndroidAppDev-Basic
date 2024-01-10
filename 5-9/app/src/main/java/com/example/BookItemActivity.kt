package com.example.hlt

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

class BookItemActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var settings: WebSettings
    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_item)

        webView = findViewById(R.id.webview)
        settings = webView.settings
        actionBar = supportActionBar!!

        settings.javaScriptEnabled = true
        settings.builtInZoomControls = true
        settings.useWideViewPort = true

        val book = intent.getSerializableExtra("book") as Book

        actionBar.title = book.title
        actionBar.subtitle = book.author

        webView.webViewClient = WebViewClient()
        webView.loadUrl(book.href)
    }
}