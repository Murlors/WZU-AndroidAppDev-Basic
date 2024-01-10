package com.example.hlt

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

data class Landscape(var name: String, var imageResId: Int)

class LandscapeAdapter(
    context: Context,
    private val resourceId: Int,
    private val nameTextId: Int,
    private val imageViewId: Int,
    data: List<Landscape>
) : ArrayAdapter<Landscape>(context, resourceId, data) {

    private class ViewHolder(val nameText: TextView, val imageView: ImageView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            viewHolder = ViewHolder(
                view.findViewById(nameTextId),
                view.findViewById(imageViewId)
            )
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
    private val landscapes = listOf(
        Landscape("West Lake", R.drawable.west_lake),
        Landscape("Ling Yin Temple", R.drawable.ling_yin_temple),
        Landscape("Qian Jiang CBD", R.drawable.qian_jiang_cbd),
        Landscape("Great Wall", R.drawable.great_wall),
        Landscape("Olympic Sports Center", R.drawable.olympic_sports_center),
        Landscape("The Imperial Palace", R.drawable.the_imperial_palace),
        Landscape("Da Lou Mountain", R.drawable.da_luo_shan),
        Landscape("Nan Xi River", R.drawable.nan_xi_river),
        Landscape("Yan Dang Mountain", R.drawable.yan_dang_shan)
    )

    private val visibleLandscape = landscapes.subList(0, 2).toMutableList()
    private lateinit var listView: ListView
    private lateinit var listViewAdapter: LandscapeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.list_view)
        listViewAdapter =
            LandscapeAdapter(this, R.layout.row_view, R.id.row_view_tv, R.id.row_view_iv, visibleLandscape)
        listView.adapter = listViewAdapter

        registerForContextMenu(listView)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        menuInflater.inflate(R.menu.ctx_menu, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val position = info.position
        val landscape = visibleLandscape[position]

        when (item.itemId) {
            R.id.ctx_delete -> {
                visibleLandscape.removeAt(position)
                listViewAdapter.notifyDataSetChanged()
            }

            R.id.ctx_modify -> {
                modifyLandscapeAlertDialog(landscape)
            }

            R.id.ctx_new -> {
                modifyLandscapeAlertDialog()
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.opt_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.opt_new -> {
                modifyLandscapeAlertDialog()
            }

            R.id.opt_reset -> {
                visibleLandscape.clear()
                visibleLandscape.addAll(landscapes.subList(0, 2))
                listViewAdapter.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun modifyLandscapeAlertDialog(landscape: Landscape? = null) {
        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_view, null)
        val nameText = view.findViewById<EditText>(R.id.dialog_view_name)
        val imageView = view.findViewById<ImageView>(R.id.dialog_view_img)
        val gridView = view.findViewById<GridView>(R.id.dialog_view_grid_view)
        val adapter = LandscapeAdapter(this, R.layout.grid_view, R.id.grid_view_name, R.id.grid_view_img, landscapes)
        gridView.adapter = adapter
        var imageResId = R.drawable.west_lake

        landscape?.let {
            nameText.setText(landscape.name)
            imageResId = landscape.imageResId
            imageView.setImageResource(imageResId)
        }

        gridView.setOnItemClickListener { _, _, position, _ ->
            imageResId = landscapes[position].imageResId
            nameText.setText(landscapes[position].name)
            imageView.setImageResource(imageResId)
        }

        builder.setView(view)
            .setPositiveButton("确定") { _, _ ->
                val name = nameText.text.toString()
                landscape?.apply {
                    this.name = name
                    this.imageResId = imageResId
                } ?: visibleLandscape.add(Landscape(name, imageResId))
                listViewAdapter.notifyDataSetChanged()
            }
            .setNegativeButton("取消", null)
            .setTitle("修改风景")
            .create()
            .show()
    }
}