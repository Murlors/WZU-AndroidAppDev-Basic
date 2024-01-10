package com.example.hlt

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    private val db: PhoneDatabase by lazy { PhoneDatabase(applicationContext) }
    var id: Long = 0

    private fun getPathFromURI(contentUri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, projection, null, null, null)
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val filePath = cursor.getString(columnIndex)
            cursor.close()
            return filePath
        }
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_layout)

        val tvCreateTime = findViewById<TextView>(R.id.tv_create_time)
        val tvModifyTime = findViewById<TextView>(R.id.tv_modify_time)
        val etTitle = findViewById<EditText>(R.id.et_title)
        val etContent = findViewById<EditText>(R.id.et_content)
        val ivImage = findViewById<ImageView>(R.id.iv_image)
        lateinit var imagePath: String

        db.open()

        val intent = intent
        val isNewData = MainActivity.getIntentIsNewData(intent)
        if (!isNewData) {
            id = MainActivity.getIntentIdData(intent)
            val phoneCursor = db.queryById(id)
            tvCreateTime.text = phoneCursor.createTime
            tvModifyTime.text = phoneCursor.modifyTime
            etTitle.setText(phoneCursor.title)
            etContent.setText(phoneCursor.content)
            imagePath = phoneCursor.imagePath
            val bitmap = BitmapFactory.decodeFile(imagePath)
            ivImage.setImageBitmap(bitmap)
        } else {
            etTitle.setText("")
            etContent.setText("")
        }

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uri = result.data?.data
                imagePath = getPathFromURI(uri!!)
                ivImage.setImageURI(uri)
            }
        }

        findViewById<Button>(R.id.bt_select_image).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            launcher.launch(intent)
        }
        findViewById<Button>(R.id.bt_ok).setOnClickListener {
            val title = etTitle.text.toString()
            val content = etContent.text.toString()
            if (!isNewData) {
                db.updateData(title, content, id, imagePath)
            } else {
                db.insertData(title, content, imagePath)
            }
            setResult(RESULT_OK)
            finish()
        }
        findViewById<Button>(R.id.bt_cancel).setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
