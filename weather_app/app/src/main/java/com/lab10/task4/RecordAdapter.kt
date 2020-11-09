package com.lab10.task4

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.app.Activity
import android.view.LayoutInflater

class RecordAdapter (context: Context, cities: List<City>): BaseAdapter() {

    private var recordContext = context
    private var cityList: MutableList<City> = cities.toMutableList()

    private class RecordViewHolder {
        lateinit var nameView: TextView
        lateinit var temperatureView: TextView
    }

    fun add(city: City) {
        cityList.add(city)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: RecordViewHolder
        var view: View? = null

        if (convertView == null) {
            val recordInflater =
                recordContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = recordInflater.inflate(R.layout.record, null)

            holder = RecordViewHolder()
            holder.temperatureView = view.findViewById(R.id.cityTemperature) as TextView
            holder.nameView = view.findViewById(R.id.cityName) as TextView
            view.tag = holder

        } else {
            holder = convertView.tag as RecordViewHolder
        }

        val record = getItem(position) as City
        holder.nameView.text = record.name
        holder.temperatureView.text = record.temperature
        if(convertView != null)
            return convertView
        return view!!
    }

    override fun getItem(position: Int): Any {
        return cityList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return cityList.size
    }
}