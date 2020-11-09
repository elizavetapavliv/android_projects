package com.android.purchaseapp

import android.app.AlertDialog
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import com.android.purchaseapp.models.DBHelper
import com.android.purchaseapp.models.Record
import com.android.purchaseapp.models.RecordAdapter
import android.widget.AdapterView

class ListActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var recordAdapter: RecordAdapter
    private lateinit var dbHelper: DBHelper
    private var updateClicked: Boolean = false
    private lateinit var productText: EditText
    private lateinit var numberText: EditText
    private lateinit var currentProduct: Record
    private var currentPosition = 0
    private lateinit var category: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        productText = findViewById<View>(R.id.productText) as EditText
        numberText = findViewById<View>(R.id.numberText) as EditText

        recordAdapter = RecordAdapter(this, ArrayList())
        listView = findViewById<View>(R.id.purchasesView) as ListView
        listView.adapter = recordAdapter
        listView.onItemClickListener = AdapterView.OnItemClickListener {  parentView: AdapterView<*>, _: View,
            position: Int, _: Long->
                currentPosition = position
                currentProduct = parentView.getItemAtPosition(position) as Record
                showChoiceDialog()
            }

        dbHelper = DBHelper.getInstance(this)
        category = intent.extras!!.getString("category")!!
        initData()
    }

    private fun initData(){
        val db = dbHelper.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM " + dbHelper.table + " WHERE category='" + category + "'", null)

        while (cursor.moveToNext()){
            recordAdapter.add(
                Record(
                    cursor.getLong(0), cursor.getString(1),
                    cursor.getInt(2)
                )
            )
        }
        cursor.close()
        db.close()
    }

    private fun showInfoDialog(message: String, title: String){
        val builder = AlertDialog.Builder(this)

        builder.setMessage(message)
            .setPositiveButton(
                resources.getString(R.string.ok)){dialog, _ ->
                dialog.dismiss()
            }

        val alert = builder.create()
        alert.setTitle(title)
        alert.show()
    }

    private fun addProduct(){
        val product =  productText.text.toString()
        val number = numberText.text.toString()
        var message = resources.getString(R.string.errorMessage)
        var title = resources.getString(R.string.error)
        var id: Long

        if(product.isNotEmpty() && number.isNotEmpty()){
            val db = dbHelper.writableDatabase
            val cv = ContentValues()
            cv.put(DBHelper.product, product)
            cv.put(DBHelper.number, number.toInt())
            cv.put(DBHelper.category, category)
            id = db.insert(dbHelper.table, null, cv)
            db.close()
            message = resources.getString(R.string.successAdd)
            title = resources.getString(R.string.success)
            recordAdapter.add(Record(id, product, number.toInt()))
        }

        showInfoDialog(message, title)

        productText.setText("")
        numberText.setText("")
    }

    private fun updateProduct(){
        val product =  productText.text.toString()
        val number = numberText.text.toString()
        var message = resources.getString(R.string.errorMessage)
        var title = resources.getString(R.string.error)

        if(product.isNotEmpty() && number.isNotEmpty()){
            updateClicked = false
            val db = dbHelper.writableDatabase
            val cv = ContentValues()
            cv.put(DBHelper.product, product)
            cv.put(DBHelper.number, number.toInt())
            cv.put(DBHelper.category, category)
            db.update(dbHelper.table, cv, "_id = ?", arrayOf(currentProduct.id.toString())).toLong()
            db.close()
            message = resources.getString(R.string.successUpdate)
            title = resources.getString(R.string.success)
            recordAdapter.update(currentPosition, Record(currentProduct.id, product, number.toInt()))
            productText.setText("")
            numberText.setText("")
        }
        else{
            printProductData()
        }
        showInfoDialog(message, title)
    }

    private fun deleteProduct(){
        val db = dbHelper.writableDatabase
        db.delete(dbHelper.table, "_id = ?", arrayOf(currentProduct.id.toString())).toLong()
        db.close()
        recordAdapter.delete(currentPosition)
        showInfoDialog(resources.getString(R.string.successDelete), resources.getString(R.string.success))
    }

    private fun printProductData(){
        productText.setText(currentProduct.product)
        numberText.setText(currentProduct.number.toString())
    }

    private fun showChoiceDialog() {
        val builder = AlertDialog.Builder(this)
        val message: String = resources.getString(R.string.message)
        val title: String = resources.getString(R.string.action)
        builder.setMessage(message)
            .setPositiveButton(resources.getString(R.string.update)){dialog, _ ->
                updateClicked = true
                printProductData()
                dialog.dismiss()
            }
            .setNegativeButton(resources.getString(R.string.delete)){dialog, _ ->
                deleteProduct()
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.setTitle(title)
        alert.show()
    }

    fun okOnClick(v: View){
        if(updateClicked){
            updateProduct()
        }
        else{
            addProduct()
        }
    }
}
