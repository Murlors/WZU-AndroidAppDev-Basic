package com.example.hlt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookViewModel: ViewModel() {
    val books = MutableLiveData<MutableList<Book>>()
}