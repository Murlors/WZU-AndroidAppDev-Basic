package com.example.hlt

data class Book(val title: String, val author: String, val href: String, val imgSrc: String) : java.io.Serializable {
    override fun toString(): String {
        return "Title=$title\n" +
                "Author=$author\n" +
                "Href=$href\n" +
                "ImgSrc=$imgSrc\n"
    }
}