package com.example.mybukuresipi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class addRecipe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "MyBukuResipi"
        //setbackbutton
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)



        val btnSave = findViewById<ImageButton>(R.id.btnSave)
        btnSave.setOnClickListener(){
            val recipename = findViewById<EditText>(R.id.inRecipeName)
            val ingredients = findViewById<EditText>(R.id.inIngredients)
            val steps = findViewById<EditText>(R.id.inSteps)

            val emptyLevel = emptiness(recipename,ingredients,steps)
            if(emptyLevel>0){
                //which field is empty
                when(emptyLevel){
                    5 -> subToast("Type your Recipe Name")
                    6 -> subToast("Type your Ingredients")
                    7 -> subToast("Type your Steps")
                    11 -> subToast("Type your Resipe Name and Ingredients")
                    12 -> subToast("Type your Resipe Name and Steps")
                    13 -> subToast("Type your Ingredients and Steps")
                    18 -> subToast("Type your Resipe Name, Ingredients and Steps")
                }
            }else{
                //check for exits recipe
                val status = checkKey(recipename.text.toString())
                val recipename = recipename.text.toString()
                val ingredients = ingredients.text.toString()
                val steps = steps.text.toString()
                if(!status) {
                    val db = openOrCreateDatabase("bukuresipi", MODE_PRIVATE, null)
                    val sql = "INSERT INTO recipe (recipename,ingredients,steps) VALUES ('$recipename','$ingredients','$steps');"
                    db.execSQL(sql)
                    subToast("New recipe name $recipename added!")
                    val intent = Intent(this, DisplayRecipe::class.java).apply {
                    }
                    startActivity(intent)

                }else {
                    subToast("Recipe already exists inside Database!")
                }

                }
            }
        }

    private fun checkKey(recipename: String):Boolean {
        val db = openOrCreateDatabase("bukuresipi", MODE_PRIVATE, null)
        val sql = "SELECT * FROM recipe where recipename='$recipename'"
        val cursor = db.rawQuery(sql, null)
        var out = false
        if(cursor.count>0)
            out=true
        return out
    }

    private fun emptiness(recipename:EditText,ingredients:EditText,steps:EditText):Int{
        var empty = 0

        if(recipename.text.isEmpty())
            empty +=5

        if(ingredients.text.isEmpty())
            empty +=6

        if(steps.text.isEmpty())
            empty +=7

        return empty

    }
    private fun subToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}

