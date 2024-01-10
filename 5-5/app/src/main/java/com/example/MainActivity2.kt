package com.example.hlt

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_2)

        listView = findViewById(R.id.list_view)

        val bundle = intent.extras
        val url = bundle?.getString(CommonValues.KEY_URL.toString()).toString()

        WebApiUtils.fetchDataAsync(this, url, object : WebApiUtils.OnFetchFinishListener {
            override fun onFetchFinish(result: Array<Locate>) {
                val adapter = ArrayAdapter(
                    this@MainActivity2,
                    android.R.layout.simple_list_item_1,
                    result
                )
                listView.adapter = adapter
            }

            override fun onFetchFailed(e: Exception) {
                Toast.makeText(this@MainActivity2, e.message, Toast.LENGTH_LONG).show()
            }
        })

        listView.setOnItemClickListener { parent, view, position, id ->
            val locate = parent.getItemAtPosition(position) as Locate
            val bundle = Bundle()
            bundle.putSerializable(CommonValues.KEY_CITY.toString(), locate)
            intent.putExtras(bundle)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}