package com.lab11

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.logging.Logger


class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_NAME " +
                    "($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_RECIPE TEXT UNIQUE, $COLUMN_CALORIES INTEGER, $COLUMN_FAT INTEGER, $COLUMN_PROTEIN INTEGER, $COLUMN_CABS INTEGER)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun saveRecipe(recipe: Recipe) {
        val values = ContentValues()
        values.put(COLUMN_RECIPE, recipe.recipe)
        values.put(COLUMN_CALORIES, recipe.calories)
        values.put(COLUMN_FAT, recipe.fat)
        values.put(COLUMN_PROTEIN, recipe.protein)
        values.put(COLUMN_CABS, recipe.cabs)

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }


    fun getAllRecipes(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "RecipesDatabase.db"
        const val TABLE_NAME = "Recipes"

        const val COLUMN_ID = "id"
        const val COLUMN_RECIPE = "recipe"
        const val COLUMN_CALORIES = "calories"
        const val COLUMN_FAT = "fat"
        const val COLUMN_PROTEIN = "protein"
        const val COLUMN_CABS = "cabs"
    }


}