package com.android.purchaseapp

import android.content.Context
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.view.LayoutInflater
import android.widget.ImageView

class Category(name: String, resource: Int) {
    val categoryName = name
    val imageResource = resource
}

class CategoryAdapter(context: Context): BaseAdapter() {

    private var categories = arrayOf(
        Category("Fruits & Vegetables", R.drawable.fruits_vegetables),
        Category("Dairy", R.drawable.dairy),
        Category("Bakery", R.drawable.bakery),
        Category("Meat & Fish", R.drawable.meat_fish),
        Category("Grocery", R.drawable.grocery),
        Category("Beverages", R.drawable.beverages))
    private val mContext: Context = context


    override fun getCount(): Int {
        return categories.size
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val category = categories[position]

        val layoutInflater = LayoutInflater.from(mContext)
        view = convertView ?: layoutInflater.inflate(R.layout.framelayout_category, null)

        val imageView = view?.findViewById<ImageView>(R.id.categoryImageView)
        val textView = view?.findViewById<TextView>(R.id.categoryTextView)

        imageView?.setImageResource(category.imageResource)
        textView?.text = category.categoryName

        return view
    }
}