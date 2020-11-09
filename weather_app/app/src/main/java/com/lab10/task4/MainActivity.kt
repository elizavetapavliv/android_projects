package com.lab10.task4

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList
import java.text.SimpleDateFormat


class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var recordAdapter: RecordAdapter
    private lateinit var citiesView: ListView
    private lateinit var cityInfo: TextView
    private lateinit var addCity: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recordAdapter = RecordAdapter(this, ArrayList())
        citiesView = findViewById<View>(R.id.citiesView) as ListView
        cityInfo = findViewById<View>(R.id.textView) as TextView
        addCity = findViewById<View>(R.id.editText) as EditText
        citiesView.adapter = recordAdapter
        citiesView.onItemClickListener = this
        WeatherTask(City("Minsk")).execute(0)
        WeatherTask(City("Pinsk")).execute(0)
    }

    fun addClick(v: View){
        WeatherTask(City(editText.text.toString())).execute(0)
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

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        showDialog(parent.adapter.getItem(position) as City)
    }

    private fun showDialog(city: City) {
        val builder = AlertDialog.Builder(this)
        val message: String = resources.getString(R.string.action)
        val title: String = city.name
        builder.setMessage(message)
            .setPositiveButton(resources.getString(R.string.forecast), DialogInterface.OnClickListener {
                    dialog, id -> WeatherTask(city).execute(2)
            })
            .setNegativeButton(resources.getString(R.string.details), DialogInterface.OnClickListener { dialog, id ->
                WeatherTask(city).execute(1)
            })
        val alert = builder.create()
        alert.setTitle(title)
        alert.show()
    }

    inner class WeatherTask(city: City): AsyncTask<Int, Void, String>() {

        private val city = city
        private val API: String = "e7fe3837712e5917b5d25505cf0f0c0d"
        private var weatherType: Int = 0

        override fun doInBackground(vararg weather: Int?): String {
            weatherType = weather[0]!!
            var myUrl: URL
            if(weatherType == 2)
                myUrl = URL("https://api.openweathermap.org/data/2.5/forecast?q=${city.name}&units=metric&appid=$API")
            else
                myUrl = URL("https://api.openweathermap.org/data/2.5/weather?q=${city.name}&units=metric&appid=$API")
            val connection = myUrl.openConnection() as HttpURLConnection
            if(connection.responseCode==404 || connection.responseCode == 400)
            {
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
            if(result == "")
            {
                showErrorDialog()
                return
            }
            super.onPostExecute(result)
            val jsonObj = JSONObject(result)
            when (weatherType) {
                0 -> {
                    val main = jsonObj.getJSONObject("main")
                    city.temperature = main.getString("temp") + "°C"
                    recordAdapter.add(city)
                    editText.setText("")
                }
                1 -> {
                    val main = jsonObj.getJSONObject("main")
                    val wind = jsonObj.getJSONObject("wind")
                    val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                    val tempMin = "Min Temp: " + main.getString("temp_min") + "°C"
                    val tempMax = "Max Temp: " + main.getString("temp_max") + "°C"
                    val pressure = "Pressure: " + main.getString("pressure") + " hPa"
                    val humidity = "Humidity: " + main.getString("humidity") + "%"
                    val windSpeed = "Wind speed: " + wind.getString("speed") + " m/s"
                    val weatherDescription = "Weather: " + weather.getString("description")

                    cityInfo.text =
                        "$tempMin \n $tempMax \n $pressure \n $humidity \n $windSpeed \n $weatherDescription"
                }
                2 -> {
                    val forecastArray = jsonObj.getJSONArray("list")
                    cityInfo.text = ""
                    for(i:Int in 0..2) {
                        val dailyForecast = forecastArray.getJSONObject((i+1)*8)
                        val dt = dailyForecast.getString("dt")
                        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
                        val date = Date(dt.toLong() * 1000L)
                        cityInfo.append("${dateFormat.format(date)}\n")
                        val main = dailyForecast.getJSONObject("main")
                        val temp =  main.getString("temp") + "°C"
                        val weather = dailyForecast.getJSONArray("weather").getJSONObject(0)
                        val weatherDescription = weather.getString("description")
                        cityInfo.append("$temp \n $weatherDescription \n\n ")
                    }
                }
            }
        }
    }

}
