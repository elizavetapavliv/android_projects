package com.android.purchaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_category.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View


class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val categoryAdapter = CategoryAdapter(this)
        grid.adapter = categoryAdapter

        grid.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra("category", position.toString())
            startActivity(intent)
        }
    }
}
