package com.lab11

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchRecipeNutritionTask : AsyncTask<String, Void, String>() {
    override fun doInBackground(vararg params: String?): String {
        var result = ""

        val url =
            URL("https://api.spoonacular.com/recipes/guessNutrition?title=${params[0]}&apiKey=1d63ee4ead864756af5f9d79bed5628a")

        val httpURLConnection = url.openConnection() as HttpURLConnection
        val responseCode: Int = httpURLConnection.responseCode
        if (responseCode == 200) {
            val inStream: InputStream = httpURLConnection.inputStream
            val isReader = InputStreamReader(inStream)
            val bReader = BufferedReader(isReader)
            var tempStr: String?

            while (true) {
                tempStr = bReader.readLine()
                if (tempStr == null) {
                    break
                }
                result += tempStr
            }
        } else {
            Log.e("Response code", responseCode.toString())
            result = responseCode.toString()
        }

        return result
    }
}