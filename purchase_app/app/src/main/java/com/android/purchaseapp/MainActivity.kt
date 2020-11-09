package com.android.purchaseapp

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var sharedPref: SharedPreferences
    lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = getPreferences(Context.MODE_PRIVATE)
        builder = AlertDialog.Builder(this@MainActivity)

        setContentView(R.layout.activity_main)
    }

    fun enterButton(view: View) {
        if (sharedPref.contains("login") && sharedPref.contains("password")) {
            val login = loginEditText.editableText.toString()
            val password = passwordEditText.editableText.toString()
            if (login == sharedPref.getString("login", null) &&
                password == sharedPref.getString("password", null)) {
                Toast.makeText(this@MainActivity, "User have successfully signed in", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, CategoryActivity::class.java)
                startActivity(intent)
            } else {
                builder.setTitle(resources.getString(R.string.error))
                        .setMessage(resources.getString(R.string.wrong_credentials_error))
                        .setCancelable(false)
                        .setNegativeButton("OK")
                        { dialog, id ->
                            dialog.cancel()
                        }
                val alert = builder.create()
                alert.show()
            }
        }
        else
        {
            builder.setTitle(resources.getString(R.string.error))
                .setMessage(resources.getString(R.string.sign_up_error))
                .setCancelable(false)
                .setNegativeButton("OK")
                { dialog, id ->
                    dialog.cancel()
                }
            val alert = builder.create()
            alert.show()
        }
    }

    fun registerButton(view: View) {
        val login = loginEditText.editableText.toString()
        val password = passwordEditText.editableText.toString()
        var registerFlag = false
        if (sharedPref.contains("login") && sharedPref.contains("password")) {

            if (login == sharedPref.getString("login", null)) {
                builder.setTitle(resources.getString(R.string.error))
                    .setMessage(resources.getString(R.string.already_signed_up_error))
                    .setCancelable(false)
                    .setNegativeButton("OK")
                    { dialog, id ->
                        dialog.cancel()
                    }
                val alert = builder.create()
                alert.show()
            }
            else registerFlag = true
        }
        else registerFlag = true

        if (registerFlag) {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putString("login", login)
            editor.putString("password", password)
            editor!!.commit()

            Toast.makeText(
                    this@MainActivity,
                    "User have successfully signed up",
                    Toast.LENGTH_LONG
            ).show()
        }
    }
}