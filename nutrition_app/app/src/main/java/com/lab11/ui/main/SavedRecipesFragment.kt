package com.lab11.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.lab11.*

class SavedRecipesFragment : Fragment() {

    lateinit var dbHandler: DBHelper
    private lateinit var pageViewModel: PageViewModel
    private lateinit var recipesView: ListView
    private lateinit var recordAdapter: RecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
        dbHandler = DBHelper(activity!!.applicationContext, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_saved_recipes, container, false)
        recordAdapter = RecordAdapter(root.context, ArrayList())
        recipesView = root.findViewById<View>(R.id.recipesView) as ListView
        recipesView.adapter = recordAdapter
        initData()
        return root
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initData() {
        val cursor = dbHandler.getAllRecipes()
        cursor!!.moveToFirst()
        recordAdapter.removeAll()

        while (!cursor.isAfterLast)
        {
            val recipe = Recipe(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_RECIPE)),
                cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CALORIES)),
                cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_FAT)),
                cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_PROTEIN)),
                cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CABS)))
            recordAdapter.add(recipe, 0)
            cursor.moveToNext()
        }
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        @JvmStatic
        fun newInstance(sectionNumber: Int): SavedRecipesFragment {
            return SavedRecipesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}