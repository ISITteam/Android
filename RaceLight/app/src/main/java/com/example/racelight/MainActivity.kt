package com.example.racelight

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        startButton.setOnClickListener {
            startActivity(Intent(this, StartActivity :: class.java))

            driverTest()
        }

        rankingsButton.setOnClickListener {
            startActivity(Intent(this, RankingsActivity :: class.java))

        }

    }

    private fun driverTest(){

        val driverName = "Bobby"

        val ref = FirebaseDatabase.getInstance().getReference("test")

        val dataID = ref.push().key


        val driver = DriverTest(dataID, driverName)

        //error occurred needed !! on dataID because of string, string ? mismatch
        ref.child(dataID!!).setValue(driver).addOnCompleteListener{
            Toast.makeText(applicationContext, "Driver Saved", Toast.LENGTH_LONG).show()
        }
    }


}
