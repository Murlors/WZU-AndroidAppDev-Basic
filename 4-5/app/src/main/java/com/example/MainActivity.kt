package com.example.hlt

import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

data class Contact(val name: String, val phone: String)
class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var listViewAdapter: ArrayAdapter<String>
    private var contacts = mutableListOf(
        Contact("张三", "12345678901"),
        Contact("李四", "12345678902"),
        Contact("王五", "12345678903"),
        Contact("赵六", "12345678904"),
        Contact("孙七", "12345678905"),
        Contact("周八", "12345678906"),
        Contact("吴九", "12345678907"),
        Contact("郑十", "12345678908")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.list_view)
        listViewAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contacts.map { "${it.name} ${it.phone}" })

        listView.adapter = listViewAdapter
        registerForContextMenu(listView)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menuInflater.inflate(R.menu.opt_menu, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val position = info.position
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_view, null)
        val name = dialogView.findViewById<EditText>(R.id.dialog_view_name)
        val phone = dialogView.findViewById<EditText>(R.id.dialog_view_phone)

        when (item.itemId) {
            R.id.opt_new -> {
                dialog.setTitle("新增联系人")
                dialog.setView(dialogView)
                dialog.setPositiveButton("确定") { _, _ ->
                    val contact = Contact(name.text.toString(), phone.text.toString())
                    contacts.add(position, contact)
                    listViewAdapter.notifyDataSetChanged()
                }
                dialog.setNegativeButton("取消", null)
                dialog.show()
            }
            R.id.opt_edit -> {
                dialog.setTitle("编辑联系人")
                dialog.setView(dialogView)
                name.setText(contacts[position].name)
                phone.setText(contacts[position].phone)
                dialog.setPositiveButton("确定") { _, _ ->
                    val contact = Contact(name.text.toString(), phone.text.toString())
                    contacts[position] = contact
                    listViewAdapter.notifyDataSetChanged()
                }
                dialog.setNegativeButton("取消", null)
                dialog.show()
            }
        }
        return super.onContextItemSelected(item)
    }
}