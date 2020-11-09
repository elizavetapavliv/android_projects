package com.lab10.task5

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper : SQLiteOpenHelper {

    companion object {
        const val id: String = "_id"
        const val singer: String = "singer"
        const val song = "song"
        const val playsNumber = "playsNumber"
        const val usersListenedNum = "usersListenedNum"

        private var sInstance: DBHelper? = null
            fun getInstance(context: Context): DBHelper {
                if (sInstance == null) {
                    sInstance = DBHelper(context)
                }
                return sInstance!!
        }
    }

    private constructor(context: Context) : super(context, "songs.db", null, 1)

    val songTable = "SONG"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE if not exists " + songTable + " (" +
                "$id integer PRIMARY KEY autoincrement," +
                "$singer text," +
                "$song text," +
                "$playsNumber integer," +
                "$usersListenedNum integer)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if(oldVersion == 1 && newVersion == 2) {
            db.execSQL("DROP TABLE IF EXISTS $songTable")
            onCreate(db);
        }
    }
}