package com.lab11

class Recipe() {
    lateinit var recipe: String
    var calories:Int = 0
    var fat :Int = 0
    var protein :Int = 0
    var cabs :Int = 0
    constructor(recipe: String, calories: Int, fat: Int, protein: Int, cabs: Int):this() {
        this.recipe = recipe
        this.calories = calories
        this.fat = fat
        this.protein = protein
        this.cabs = cabs
    }
}