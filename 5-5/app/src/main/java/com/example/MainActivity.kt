package com.example.hlt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var launcher: ActivityResultLauncher<Intent>

    private val baseUrl = "http://guolin.tech/api/china/17"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.list_view)
        WebApiUtils.fetchDataAsync(this, baseUrl, object : WebApiUtils.OnFetchFinishListener {
            override fun onFetchFinish(result: Array<Locate>) {
                val adapter = ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_list_item_1,
                    result
                )
                listView.adapter = adapter
            }

            override fun onFetchFailed(e: Exception) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
        })

        iniActivityLauncher()

        listView.setOnItemClickListener { parent, view, position, id ->
            val locate = parent.getItemAtPosition(position) as Locate
            val locateUrl = "${baseUrl}/${locate.id}"
            val bundle = Bundle()
            bundle.putString(CommonValues.KEY_URL.toString(), locateUrl)
            val intent = Intent(applicationContext, MainActivity2::class.java)
            intent.putExtras(bundle)
            launcher.launch(intent)
        }
    }

    private fun iniActivityLauncher() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val bundle = data?.extras
                val locate = bundle?.getSerializable(CommonValues.KEY_CITY.toString()) as Locate
                showSnackBar(locate)
            }
        }
    }

    private fun showSnackBar(locate: Locate?) {
        val snackBar = Snackbar.make(listView, "City: ${locate?.name}", Snackbar.LENGTH_LONG)
            .setAction("Weather_id") {
                Toast.makeText(this, "Weather_id=${locate?.weather_id}", Toast.LENGTH_LONG).show()
            }.show()
    }
}