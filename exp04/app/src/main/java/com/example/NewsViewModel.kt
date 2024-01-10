package com.example.hlt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewsViewModel : ViewModel() {
    val news = MutableLiveData<MutableList<News>>()
}