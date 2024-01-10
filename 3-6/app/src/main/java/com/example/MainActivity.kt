package com.example.hlt

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

data class CityInfo(val name: String, val imageId: Int, val phone: String)

class CityAdapter(context: Context, private val resourceId: Int, data: List<CityInfo>) :
    ArrayAdapter<CityInfo>(context, resourceId, data) {

    private class ViewHolder(view: View) {
        val nameText: TextView = view.findViewById(R.id.row_view_tv_name)
        val phoneText: TextView = view.findViewById(R.id.row_view_tv_phone)
        val imageView: ImageView = view.findViewById(R.id.row_view_iv)
        val callView: ImageView = view.findViewById(R.id.row_view_iv_call)
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
        val cityInfo = getItem(position) ?: return view

        with(viewHolder) {
            nameText.text = cityInfo.name
            phoneText.text = cityInfo.phone
            imageView.setImageResource(cityInfo.imageId)
            callView.visibility = if (cityInfo.phone.isEmpty()) View.GONE else View.VISIBLE

            callView.setOnClickListener {
                Toast.makeText(context, "Calling ${cityInfo.phone}", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var tv: TextView
    private lateinit var listView: ListView

    private val cities = listOf(
        CityInfo("杭州", R.drawable.hangzhou, "0571-12345678"),
        CityInfo("宁波", R.drawable.ningbo, "0574-12345678"),
        CityInfo("温州", R.drawable.wenzhou, "0577-12345678"),
        CityInfo("杭州", R.drawable.hangzhou, ""),
        CityInfo("宁波", R.drawable.ningbo, ""),
        CityInfo("温州", R.drawable.wenzhou, ""),
        CityInfo("杭州", R.drawable.hangzhou, "0571-12345678"),
        CityInfo("宁波", R.drawable.ningbo, "0574-12345678"),
        CityInfo("温州", R.drawable.wenzhou, "0577-12345678"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById(R.id.tv)
        listView = findViewById(R.id.listView)

        val cityAdapter = CityAdapter(this, R.layout.row_view, cities)
        listView.adapter = cityAdapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val name = cities[position].name
            Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
        }
    }
}