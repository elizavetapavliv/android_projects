package com.lab10.task5

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.ArrayList
import android.widget.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var recordAdapter: RecordAdapter
    private lateinit var songsView: ListView
    private lateinit var searchSinger: EditText
    private lateinit var searchButton: Button
    private lateinit var dbHelper: DBHelper
    private var internetIsOn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper.getInstance(this)
        recordAdapter = RecordAdapter(this, ArrayList())
        songsView = findViewById<View>(R.id.songsView) as ListView
        searchSinger = findViewById<View>(R.id.editText) as EditText
        searchButton = findViewById<View>(R.id.searchButton) as Button
        songsView.adapter = recordAdapter
        songsView.onItemClickListener = this
        if(!isNetworkAvailable(this)) {
            internetIsOn = false
            Toast.makeText(this, "No Internet connection. See your saved songs",
                Toast.LENGTH_LONG).show()
            searchSinger.isEnabled = false
            searchButton.isEnabled = false
        }
        initData()
    }

    private fun isNetworkAvailable(context: Context) : Boolean {
        val connectivityManager: ConnectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.getActiveNetworkInfo()
        return activeNetworkInfo != null
    }

    private fun initData() {
        val db = dbHelper.writableDatabase
        var cursor = db.rawQuery(
            "SELECT * FROM " + dbHelper.songTable, null
        )
        if(cursor.count==0) {
            if(!internetIsOn)
                Toast.makeText(this, "No saved songs", Toast.LENGTH_LONG).show()
            else{
                SongTask(Song("Camila Cabello")).execute(true)
                SongTask(Song("Kaleo")).execute(true)
            }
        }
        cursor.close()
        db.close()
    }

    fun addToDb(song: Song){
        val cv = ContentValues()
        cv.put(DBHelper.singer, song.singer)
        cv.put(DBHelper.song, song.song)
        cv.put(DBHelper.playsNumber, song.playsNumber)
        cv.put(DBHelper.usersListenedNum, song.userListenedNum)
        val db = dbHelper.writableDatabase
        db.insert(dbHelper.songTable, null, cv)
    }

    fun savedSongsClick(v:View){
        startActivity(Intent(this, SongsActivity::class.java))
    }

    fun searchClick(v:View){
        SongTask(Song(searchSinger.text.toString())).execute(false)
    }

    private fun showErrorDialog() {
        val builder = AlertDialog.Builder(this)
        val message: String = resources.getString(R.string.errorMessage)
        val title: String = resources.getString(R.string.error)
        builder.setMessage(message)
            .setPositiveButton(resources.run { getString(R.string.ok) }, DialogInterface.OnClickListener {
                    dialog, id ->
                editText.setText("")
                dialog.dismiss()
            })
        val alert = builder.create()
        alert.setTitle(title)
        alert.show()
    }

    inner class SongTask(song: Song): AsyncTask<Boolean, Void, String>() {

        private val song = song
        private val API: String = "f3d2ab5888e0722d9a89c2d82cdf6e43"
        private var save = false

        override fun doInBackground(vararg args: Boolean?): String {
            save = args[0]!!
            var myUrl =
                URL("http://ws.audioscrobbler.com/2.0/?method=artist.gettoptracks&artist=${song.singer}&api_key=$API&format=json")

            val connection = myUrl.openConnection() as HttpURLConnection
            if (connection.responseCode == 404 || connection.responseCode == 400) {
                return ""
            }
            val stream = BufferedInputStream(connection.inputStream)
            val bufferedReader = BufferedReader(InputStreamReader(stream))
            val builder = StringBuilder()
            var inputString: String?
            while (true) {
                inputString = bufferedReader.readLine()
                if (inputString == null)
                    break
                builder.append(inputString)
            }
            return builder.toString()
        }

        override fun onPostExecute(result: String?) {
            if (result == "" || result!!.contains("error")) {
                showErrorDialog()
                return
            }
            super.onPostExecute(result)
            val jsonObj = JSONObject(result)
            val toptracks = jsonObj.getJSONObject("toptracks")
            val trackArray = toptracks.getJSONArray("track")
            recordAdapter.removeAll()
            for (i: Int in 0..9) {
                val track = trackArray.getJSONObject(i)
                song.song = track.getString("name")
                song.playsNumber = track.getInt("playcount")
                song.userListenedNum = track.getInt("listeners")
                if(save){
                   addToDb(song)
                }
                else{
                    recordAdapter.add(Song(song.singer, song.song, song.playsNumber, song.userListenedNum))
                }
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val song = parent.adapter.getItem(position) as Song
        addToDb(song)
        showDialog(song)
    }

    private fun showDialog(song: Song) {
        val builder = AlertDialog.Builder(this)
        val message: String = resources.getString(R.string.action)
        val title: String = "${song.singer}: ${song.song}"
        builder.setMessage(message)
            .setPositiveButton(resources.getString(R.string.ok), DialogInterface.OnClickListener {
                    dialog, id -> dialog.dismiss()
            })
        val alert = builder.create()
        alert.setTitle(title)
        alert.show()
    }
}