package com.lab10.task5

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.app.Activity
import android.view.LayoutInflater
import java.io.Serializable

class RecordAdapter (context: Context, songs: List<Song>): BaseAdapter(), Serializable  {

    private var recordContext = context
    private var songList: MutableList<Song> = songs.toMutableList()

    private class RecordViewHolder {
        lateinit var singerView: TextView
        lateinit var songView: TextView
        lateinit var playsNumberView: TextView
        lateinit var usersListenedNum: TextView
    }

    fun add(song: Song, i: Int) {
        songList.add(i, song)
        notifyDataSetChanged()
    }


    fun add(song: Song) {
        songList.add(song)
        notifyDataSetChanged()
    }

    fun removeAll(){
        songList.clear()
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: RecordViewHolder
        var view: View? = null

        if (convertView == null) {
            val recordInflater =
                recordContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = recordInflater.inflate(R.layout.song, null)

            holder = RecordViewHolder()
            holder.singerView = view.findViewById(R.id.singer) as TextView
            holder.songView = view.findViewById(R.id.songName) as TextView
            holder.playsNumberView = view.findViewById(R.id.playsNumber) as TextView
            holder.usersListenedNum = view.findViewById(R.id.userListenedNumber) as TextView
            view.tag = holder

        } else {
            holder = convertView.tag as RecordViewHolder
        }

        val record = getItem(position) as Song
        holder.songView.text = record.song
        holder.singerView.text = record.singer
        holder.playsNumberView.text = record.playsNumber.toString()
        holder.usersListenedNum.text = record.userListenedNum.toString()
        if(convertView != null)
            return convertView
        return view!!
    }

    override fun getItem(position: Int): Any {
        return songList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return songList.size
    }
}