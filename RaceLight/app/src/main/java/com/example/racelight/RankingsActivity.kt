package com.example.racelight

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.rankings_page.*
import org.xmlpull.v1.sax2.Driver
import java.lang.StringBuilder

class RankingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rankings_page)
        showRanksButton.setOnClickListener {
            //need to implement
            pullData()
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

                    /*for(driver in driverList){

                        textList.append(driver.name)
                        textList.append('\n')
                        textList.append(driver.reactionTime.toString())
                        textList.append('\n')
                    }*/

                    rankingsText.text = textList.toString()
                }
            }
        })

    }

    }