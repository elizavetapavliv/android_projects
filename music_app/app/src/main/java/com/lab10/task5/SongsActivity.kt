package com.lab10.task5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast

class SongsActivity : AppCompatActivity() {

    private lateinit var songsView: ListView
    private lateinit var dbHelper: DBHelper
    private lateinit var recordAdapter: RecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songs)

        dbHelper = DBHelper.getInstance(this)
        recordAdapter = RecordAdapter(this, ArrayList())
        songsView = findViewById<View>(R.id.savedSongsView) as ListView
        songsView.adapter = recordAdapter
        initData()
    }

    private fun initData() {
        val db = dbHelper.writableDatabase
        var cursor = db.rawQuery(
            "SELECT * FROM " + dbHelper.songTable, null
        )
        while (cursor.moveToNext()) {
            recordAdapter.add(
                Song(
                    cursor.getString(1),
                    cursor.getString(2), cursor.getInt(3),
                    cursor.getInt(4)
                ), 0
            )
        }
        cursor.close()
        db.close()
    }
}
