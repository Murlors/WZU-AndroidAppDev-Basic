package com.example.hlt

data class Locate(val name: String, val id: String, val weather_id: String) : java.io.Serializable {
    override fun toString(): String {
        return "$name,id=$id"
    }
}