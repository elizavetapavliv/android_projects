package com.android.purchaseapp.models

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.app.Activity
import android.view.LayoutInflater
import com.android.purchaseapp.R

class RecordAdapter (context: Context, records: List<Record>): BaseAdapter() {

    private var recordContext = context
    private var recordList: MutableList<Record> = records.toMutableList()

    private class RecordViewHolder {
        lateinit var productView: TextView
        lateinit var numberView: TextView
    }

    fun add(record: Record) {
        recordList.add(record)
        notifyDataSetChanged()
    }

    fun update(position: Int, record: Record){
        recordList[position] = record
        notifyDataSetChanged()
    }

    fun delete(position: Int){
        recordList.removeAt(position)
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
            holder.productView = view.findViewById(R.id.recordProduct) as TextView
            holder.numberView = view.findViewById(R.id.recordNumber) as TextView
            view.tag = holder

        } else {
            holder = convertView.tag as RecordViewHolder
        }

        val record = getItem(position) as Record
        holder.productView.text = record.product
        holder.numberView.text = record.number.toString()
        if(convertView != null)
            return convertView
        return view!!
    }

    override fun getItem(position: Int): Any {
        return recordList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return recordList.size
    }
}