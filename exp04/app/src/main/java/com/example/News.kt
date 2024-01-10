package com.example.hlt

data class News(val title: String, val date: String, val href: String, val imgSrc: String) :
    java.io.Serializable {
    override fun toString(): String {
        return "Title=$title\n" +
                "Date=$date\n" +
                "Href=$href\n" +
                "ImgSrc=$imgSrc\n"
    }
}