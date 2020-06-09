package com.example.racelight

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.rankings_page.*
import org.xmlpull.v1.sax2.Driver
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.StringBuilder

class RankingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rankings_page)
        showRanksButton.setOnClickListener {
            readLocalData()
            pullData()
        }
    }

    private fun readLocalData(){
       /* val file = "driver.txt"
        val textFromLocal: String = application.assets.open(file).bufferedReader().use{
            it.readText()
        }*/
        //val filePath = "src/resources/driverFile.txt"
        //val driverFile = File(filePath)
        val fileName = "driverText.txt"
        val fileInput = applicationContext.openFileInput(fileName)
        val bufferedReader = BufferedReader(InputStreamReader(fileInput))
        val localStrBuilder = StringBuilder()
        var text: String? = null

        while({text = bufferedReader.readLine(); text}() != null){
            localStrBuilder.append(text)
        }
        //val textFromLocal:String = applicationContext.openFileInput(fileName).read().toString(Charsets.UTF_8)
        val textFromLocal = localStrBuilder.toString()
        if(textFromLocal.isNotEmpty()){
            driverText.text = textFromLocal
        }
        //test for empty file
        /*if(textFromLocal.isEmpty()){
            driverText.text = "I'm Empty!"
        }*/

        applicationContext.openFileOutput(fileName, Context.MODE_PRIVATE).use{
            it.write(("").toByteArray())
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
                        val reactionTime = seconds.toString() //+ "." + miliseconds.toString()
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