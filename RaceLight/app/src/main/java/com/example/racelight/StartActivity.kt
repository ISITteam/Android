package com.example.racelight

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.start_page.*
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.util.*

class StartActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_page)


        launchButton.setOnClickListener{
            driverTest()
        }
    }

    private fun driverTest(){

        val driverName = "Bobby"
        val driverTime = .2

        val driverDate = Date()

        val ref = FirebaseDatabase.getInstance().getReference("Drivers")

        val dataID = ref.push().key

        val driver = DriverModel(dataID, driverName, driverTime, driverDate)

        //error occurred needed !! on dataID because of string, string ? mismatch
        ref.child(dataID!!).setValue(driver).addOnCompleteListener{
            Toast.makeText(applicationContext, "Driver Saved", Toast.LENGTH_LONG).show()
        }
    }

}