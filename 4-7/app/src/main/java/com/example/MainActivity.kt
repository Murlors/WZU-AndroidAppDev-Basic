package com.example.hlt

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

data class Person(val name: String, val id: String)
class MainActivity : AppCompatActivity() {
    private lateinit var tvResult: TextView
    private lateinit var listView: ListView
    private lateinit var personAdapter: PersonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val personList = (0..9).map { Person("Tom$it", "%04d".format(it)) }
        tvResult = findViewById(R.id.tv_result)
        listView = findViewById(R.id.list_view)

        personAdapter = PersonAdapter(this, R.layout.row_view, personList)
        listView.adapter = personAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.opt_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val sendMenuItem = menu?.findItem(R.id.opt_send)
        val selectedContactsCount = personAdapter.getSelectedPersons().size
        sendMenuItem?.title = "发送 ($selectedContactsCount)"
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.opt_select_all -> personAdapter.selectAll()
            R.id.opt_select_none -> personAdapter.selectNone()
            R.id.opt_select_reverse -> personAdapter.selectReverse()
            R.id.opt_send -> {
                val selectedPersons = personAdapter.getSelectedPersons()
                val selectedPersonsStr = selectedPersons.joinToString { "${it.name}:${it.id}" }
                tvResult.text = "[${selectedPersonsStr}]"
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

class PersonAdapter(context: Context, private val resourceId: Int, data: List<Person>) :
    ArrayAdapter<Person>(context, resourceId, data) {

    private val selectedPersonsMap = mutableMapOf<Int, Boolean>()

    fun getSelectedPersons(): List<Person> {
        return selectedPersonsMap
            .filterValues { it }
            .mapNotNull { getItem(it.key) }
    }

    private class ViewHolder(view: View) {
        val nameText: TextView = view.findViewById(R.id.row_view_name)
        val idText: TextView = view.findViewById(R.id.row_view_id)
        val checkBox: CheckBox = view.findViewById(R.id.row_view_check_box)
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

        val person = getItem(position) ?: return view

        with(viewHolder) {
            nameText.text = person.name
            idText.text = person.id
            checkBox.isChecked = selectedPersonsMap[position] ?: false

            checkBox.setOnClickListener {
                selectedPersonsMap[position] = checkBox.isChecked
            }
        }
        return view
    }

    fun selectAll() {
        for (i in 0 until count) {
            selectedPersonsMap[i] = true
        }
        notifyDataSetChanged()
    }

    fun selectNone() {
        for (i in 0 until count) {
            selectedPersonsMap[i] = false
        }
        notifyDataSetChanged()
    }

    fun selectReverse() {
        for (i in 0 until count) {
            selectedPersonsMap[i] = !(selectedPersonsMap[i] ?: false)
        }
        notifyDataSetChanged()
    }
}