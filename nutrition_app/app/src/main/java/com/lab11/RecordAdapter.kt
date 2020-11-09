package com.lab11

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.app.Activity
import android.view.LayoutInflater
import java.io.Serializable

class RecordAdapter (context: Context, songs: List<Recipe>): BaseAdapter(), Serializable  {

    private var recordContext = context
    private var recipeList: MutableList<Recipe> = songs.toMutableList()

    private class RecordViewHolder {
        lateinit var recipeView: TextView
        lateinit var caloriesView: TextView
        lateinit var fatView: TextView
        lateinit var proteinView: TextView
        lateinit var cabsView: TextView
    }

    fun add(song: Recipe, i: Int) {
        recipeList.add(i, song)
        notifyDataSetChanged()
    }

    fun add(song: Recipe) {
        recipeList.add(song)
        notifyDataSetChanged()
    }

    fun removeAll(){
        recipeList.clear()
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: RecordViewHolder
        var view: View? = null

        if (convertView == null) {
            val recordInflater =
                recordContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = recordInflater.inflate(R.layout.recipe, null)

            holder = RecordViewHolder()
            holder.recipeView = view.findViewById(R.id.recipe) as TextView
            holder.caloriesView = view.findViewById(R.id.calories) as TextView
            holder.fatView = view.findViewById(R.id.fat) as TextView
            holder.proteinView = view.findViewById(R.id.protein) as TextView
            holder.cabsView = view.findViewById(R.id.cabs) as TextView
            view.tag = holder

        } else {
            holder = convertView.tag as RecordViewHolder
        }

        val record = getItem(position) as Recipe
        holder.recipeView.text = record.recipe
        holder.caloriesView.text = record.calories.toString()
        holder.fatView.text = "${record.fat} g"
        holder.proteinView.text =  "${record.protein} g"
        holder.cabsView.text =  "${record.cabs} g"
        if(convertView != null)
            return convertView
        return view!!
    }

    override fun getItem(position: Int): Any {
        return recipeList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return recipeList.size
    }
}