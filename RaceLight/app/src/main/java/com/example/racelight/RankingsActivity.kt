package com.example.racelight

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.rankings_page.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class RankingsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rankings_page)

        showRanksButton.setOnClickListener {
            readLocalData()
            pullData()
        }


        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity :: class.java))


        }
    }

    private fun readLocalData(){
        val fileName = "driverText.txt"
        val file = baseContext.getFileStreamPath(fileName)

        if(file.exists()){
            applicationContext.openFileInput(fileName).use { stream ->
                val textFromLocal = stream.bufferedReader().use {
                    it.readText()
                }
                if(textFromLocal.isNotEmpty()){
                    driverText.text = textFromLocal.toString()
                }
            }

            applicationContext.openFileOutput(fileName, Context.MODE_PRIVATE).use{
                it.write(("").toByteArray())
            }
        }
    }

    private fun pullData(){
        val driverList = mutableListOf<DriverModel>()//mutable list to use the add function
        val textList = StringBuilder()
        val query = FirebaseDatabase.getInstance().getReference("Drivers").orderByChild("reactionTime").limitToFirst(5)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()) {
                    for (data in snapshot.children) {
                        val driver = data.getValue(DriverModel::class.java)
                        driverList.add(driver!!)
                    }

                    for((index, driver) in driverList.withIndex())
                    {
                        val seconds = driver.reactionTime / 1000
                        val miliseconds = driver.reactionTime % 1000
                        val reactionTime = "${seconds.toString() + "." + miliseconds.toString()}"
                        textList.append(index + 1).append(".").append(" ")
                        textList.append(driver.name).append(" ").append(reactionTime)
                        textList.append('\n')
                    }

                    rankingsText.text = textList.toString()
                }
            }
        })

    }

}