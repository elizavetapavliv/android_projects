package com.android.purchaseapp.models

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper : SQLiteOpenHelper {

    companion object {
        const val id: String = "_id"
        const val product: String = "product"
        const val number = "product_number"
        const val category = "category"
        private var sInstance: DBHelper? = null
        fun getInstance(context: Context): DBHelper {
            if (sInstance == null) {
                sInstance =
                    DBHelper(context)
            }
            return sInstance!!
        }
    }

    private constructor(context: Context) : super(context, "purchases.db", null, 2)

    val table = "Purchase"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE if not exists " + table + " (" +
                "$id integer PRIMARY KEY autoincrement," +
                "$product text," +
                "$number integer," +
                "$category text)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if(oldVersion == 1 && newVersion == 2){
            db.execSQL( "DROP TABLE IF EXISTS $table")
            onCreate(db);
        }
    }
}