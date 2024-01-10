package com.example.hlt

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

data class City(val name: String, val landscapes: List<Landscape>)
data class Landscape(val name: String, val imageResId: Int)

class LandscapeAdapter(context: Context, private val resourceId: Int, data: List<Landscape>) :
    ArrayAdapter<Landscape>(context, resourceId, data) {

    private class ViewHolder(view: View) {
        val nameText: TextView = view.findViewById(R.id.row_view_tv)
        val imageView: ImageView = view.findViewById(R.id.row_view_iv)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        val landscape = getItem(position) ?: return view

        with(viewHolder) {
            nameText.text = landscape.name
            imageView.setImageResource(landscape.imageResId)
        }

        return view
    }
}

class MainActivity : AppCompatActivity() {
    private val cities = listOf(
        City(
            "HangZhou", listOf(
                Landscape("West Lake", R.drawable.west_lake),
                Landscape("Ling Yin Temple", R.drawable.ling_yin_temple),
                Landscape("Qian Jiang CBD", R.drawable.qian_jiang_cbd),
            )
        ),
        City(
            "BeiJing", listOf(
                Landscape("Great Wall", R.drawable.great_wall),
                Landscape("Olympic Sports Center", R.drawable.olympic_sports_center),
                Landscape("The Imperial Palace", R.drawable.the_imperial_palace),
            )
        ),
        City(
            "WenZhou", listOf(
                Landscape("Da Lou Mountain", R.drawable.da_luo_shan),
                Landscape("Nan Xi River", R.drawable.nan_xi_river),
                Landscape("Yan Dang Mountain", R.drawable.yan_dang_shan),
            )
        )
    )

    private lateinit var textView: TextView
    private lateinit var spinner: Spinner
    private lateinit var imageView: ImageView
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.text_view)
        spinner = findViewById(R.id.spinner)
        imageView = findViewById(R.id.image_view)
        listView = findViewById(R.id.list_view)

        val cityNames = cities.map { it.name }
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cityNames)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val city = cities[position]
                var selectedLandscape = city.landscapes[0]

                textView.text = "${city.name}:${selectedLandscape.name}"
                imageView.setImageResource(selectedLandscape.imageResId)
                listView.adapter = LandscapeAdapter(this@MainActivity, R.layout.row_view, city.landscapes)

                listView.setOnItemClickListener { _, _, position, _ ->
                    selectedLandscape = city.landscapes[position]
                    imageView.setImageResource(selectedLandscape.imageResId)
                    textView.text = "${city.name}:${selectedLandscape.name}"
                }
            }
        }
    }
}