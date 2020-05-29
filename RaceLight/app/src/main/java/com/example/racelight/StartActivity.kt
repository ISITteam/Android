package com.example.racelight

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.start_page.*

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

        val ref = FirebaseDatabase.getInstance().getReference("test")

        val dataID = ref.push().key


        val driver = DriverTest(dataID, driverName)

        //error occurred needed !! on dataID because of string, string ? mismatch
        ref.child(dataID!!).setValue(driver).addOnCompleteListener{
            Toast.makeText(applicationContext, "Driver Saved", Toast.LENGTH_LONG).show()
        }
    }

}