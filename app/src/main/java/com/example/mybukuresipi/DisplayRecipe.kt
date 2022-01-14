package com.example.mybukuresipi

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

class DisplayRecipe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_recipe)
        var recipenames = ArrayList<String>()

        if(!dbExists(this, "bukuresipi")){
            createDB()
        }

        val db:SQLiteDatabase = openOrCreateDatabase("bukuresipi", MODE_PRIVATE, null)
        val sql = "SELECT recipename from recipe"
        var c: Cursor = db.rawQuery(sql, null)
        while (c.moveToNext()){
            val recipename = c.getString(0)
            recipenames.add(recipename)
        }
        c.close()

        val myAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, recipenames)
        val lv = findViewById<ListView>(R.id.lv)
        lv.setAdapter(myAdapter)
        lv.onItemClickListener = AdapterView.OnItemClickListener{ adapter, v, position, arg3 ->
            val value = adapter.getItemAtPosition(position).toString()
            val intent = Intent(this, viewRecipe::class.java).apply {
                putExtra("recipename", value.toString())
            }
            startActivity(intent)
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab1)
        fab.setOnClickListener(){
            val intent = Intent(this, addRecipe::class.java).apply {
            }
            startActivity(intent)
        }

    }

    private fun dbExists(c: Context, dbName: String):Boolean{
        val dbFile: File = c.getDatabasePath(dbName)
        return dbFile.exists()
    }

    private fun createDB(){
        val db = openOrCreateDatabase("bukuresipi", MODE_PRIVATE, null)
        subToast("Database bukuresipi created")
        val sqlText = "CREATE TABLE IF NOT EXISTS recipe" +
                "(recipename VARCHAR(30) PRIMARY KEY, " +
                "ingredients VARCHAR(30) NOT NULL," +
                "steps VARCHAR(30) NOT NULL" +
                ");"
        subToast("Table recipe created")
        db.execSQL(sqlText)
        var nextSQL = "INSERT INTO recipe(recipename, ingredients, steps) VALUES ('Choclate Cake','flour,sugar','mix all');"
        db.execSQL(nextSQL)
        nextSQL = "INSERT INTO recipe(recipename, ingredients, steps) VALUES ('French Fries','Fries,oil','fry');"
        db.execSQL(nextSQL)
        nextSQL = "INSERT INTO recipe(recipename, ingredients, steps) VALUES ('Nugget','nugget,oil','fry');"
        db.execSQL(nextSQL)
        subToast("3 sample recipenames added!")

    }
    private fun subToast(msg: String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }
}