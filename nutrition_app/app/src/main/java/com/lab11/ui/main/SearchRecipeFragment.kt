package com.lab11.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import androidx.lifecycle.ViewModelProviders
import android.view.MotionEvent
import com.lab11.*
import kotlinx.android.synthetic.main.fragment_search_recipe.*
import org.json.JSONObject

class SearchRecipeFragment : Fragment() {

    lateinit var dbHandler: DBHelper
    private lateinit var recipe: Recipe
    lateinit var recipeJSONObject: JSONObject

    private fun extractValue(type: String): Double {
        return recipeJSONObject.getJSONObject(type).getDouble("value")
    }

    private fun onSearchClick() {
        val recipeName = editText.text.toString()

        val result = SearchRecipeNutritionTask().execute(recipeName).get()
        recipeJSONObject = JSONObject(result)

        if (recipeJSONObject.has("status")) {
            showErrorDialog()
            return
        }
        // see structure here: https://spoonacular.com/food-api/docs#Guess-Nutrition-by-Dish-Name

        caloriesTextView.text = extractValue("calories").toString()
        fatTextView.text = extractValue("fat").toString()
        proteinTextView.text = extractValue("protein").toString()
        cabsTextView.text = extractValue("carbs").toString()
        
        Toast.makeText(context, "Double tap to save recipe",
                Toast.LENGTH_LONG).show()
        
        recipe = Recipe(editText.text.toString(),
            extractValue("calories").toInt(),
            extractValue("fat").toInt(),
            extractValue("protein").toInt(),
            extractValue("carbs").toInt())
    }

    private lateinit var pageViewModel: PageViewModel
    private lateinit var editText: EditText
    private lateinit var calories: TextView
    private lateinit var fat: TextView
    private lateinit var protein: TextView
    private lateinit var cabs: TextView
    private lateinit var searchRecipe: EditText
    private lateinit var searchButton: Button
    private lateinit var mDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(SavedRecipesFragment.ARG_SECTION_NUMBER) ?: 1)
        }
        dbHandler = DBHelper(activity!!.applicationContext, null)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_search_recipe, container, false)
        editText = root.findViewById<View>(R.id.editText) as EditText
        calories = root.findViewById<View>(R.id.caloriesTextView) as TextView
        fat = root.findViewById<View>(R.id.fatTextView) as TextView
        protein = root.findViewById<View>(R.id.proteinTextView) as TextView
        cabs = root.findViewById<View>(R.id.cabsTextView) as TextView
        searchRecipe = root.findViewById<View>(R.id.editText) as EditText
        searchButton = root.findViewById<View>(R.id.searchButton) as Button
        searchButton.setOnClickListener {
            onSearchClick()
        }

        val listener = GestureListener()
        mDetector = GestureDetectorCompat(activity, listener)
        mDetector.setOnDoubleTapListener(listener)
        class TouchListener: View.OnTouchListener{
            override fun onTouch(v:View, e: MotionEvent): Boolean {
                return mDetector.onTouchEvent(e)
            }
        }
        root.setOnTouchListener(TouchListener())
        return root
    }

    fun turnOffButtons(){
        searchRecipe.isEnabled = false
        searchButton.isEnabled = false
    }

    private fun showErrorDialog() {
        val builder = AlertDialog.Builder(context)
        val message: String = resources.getString(R.string.errorMessage)
        val title: String = resources.getString(R.string.error)
        builder.setMessage(message)
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
                editText.setText("")
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.setTitle(title)
        alert.show()
    }

    private fun showDialog(song: Recipe) {
        val builder = AlertDialog.Builder(context)
        val message: String = resources.getString(R.string.action)
        val title: String = song.recipe
        builder.setMessage(message)
            .setPositiveButton(resources.getString(R.string.ok)){dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.setTitle(title)
        alert.show()
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener(){

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            showDialog(recipe)

            dbHandler.saveRecipe(recipe)

            return true
        }

        override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
            return true
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
