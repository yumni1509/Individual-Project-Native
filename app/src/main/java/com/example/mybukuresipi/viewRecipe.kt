package com.example.mybukuresipi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class viewRecipe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_recipe)
        val intent = this.getIntent()
        val recipename = intent.getStringExtra("recipename")

        supportActionBar?.setTitle(recipename)

        //read database
        val db = openOrCreateDatabase("bukuresipi", MODE_PRIVATE, null)
        val sql = "SELECT ingredients, steps from recipe where recipename='$recipename'"
        val cursor = db.rawQuery(sql, null)

        var ingredients = ""
        var steps = ""

        while (cursor.moveToNext()){
            ingredients = cursor.getString(0)
            steps = cursor.getString(1)
        }
        cursor.close()

        var bahan = findViewById<EditText>(R.id.inIngredient)
        var cara = findViewById<EditText>(R.id.inStep)

        bahan.setText(ingredients)
        cara.setText(steps)

        val btnDel = findViewById<FloatingActionButton>(R.id.delBtn)
        var delDialog : AlertDialog? = null
        val delBuilder = AlertDialog.Builder(this)
        delBuilder.setTitle("Delete Process")
        delBuilder.setMessage("Are you sure to delete?")

        delBuilder.setNeutralButton("Cancel"){dialogInterface, which ->
            subToast("Delete Cancelled")
        }

        delBuilder.setPositiveButton("Yes"){dialogInterface, which ->
            val db = openOrCreateDatabase("bukuresipi", MODE_PRIVATE, null)
            val sql = "DELETE FROM recipe where recipename = '$recipename';"
            db.execSQL(sql)

            subToast("Recipe Name $recipename deleted!")
            val intent = Intent(this, DisplayRecipe::class.java).apply {
            }
            startActivity(intent)
        }

        delDialog = delBuilder.create()
        btnDel.setOnClickListener(){
            delDialog.show()
        }

        //=======================================================
        val btnEdit = findViewById<FloatingActionButton>(R.id.editBtn)
        var editDialog : AlertDialog? = null
        val editBuilder = AlertDialog.Builder(this)
        editBuilder.setTitle("Update Process")
        editBuilder.setMessage("Are you sure to update the data?")

        editBuilder.setNeutralButton("Cancel"){dialogInterface, which ->
            subToast("Update Cancelled")
        }

        editBuilder.setPositiveButton("Yes"){dialogInterface, which ->

            var bahan = findViewById<EditText>(R.id.inIngredient)
            var cara = findViewById<EditText>(R.id.inStep)

            val ing = bahan.text.toString()
            val st = cara.text.toString()

            val db = openOrCreateDatabase("bukuresipi", MODE_PRIVATE, null)
            val sql = "UPDATE recipe SET ingredients = '$ing', steps = '$st' WHERE recipename = '$recipename';"
            db.execSQL(sql)

            subToast("Recipe Name $recipename updated!")
            val intent = Intent(this, DisplayRecipe::class.java).apply {
            }
            startActivity(intent)
        }

        editDialog = editBuilder.create()
        btnEdit.setOnClickListener(){
            editDialog.show()
        }
    }
    private fun subToast(msg: String){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()

    }
}