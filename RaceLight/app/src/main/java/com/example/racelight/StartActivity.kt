package com.example.racelight

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.start_page.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.*

class StartActivity: AppCompatActivity(), SensorEventListener {
    //10 is a pretty vigorous shake, 5 is a little softer than i think i'd like,  3 might still randomly trigger.
    private var shakeThreshold = 7;

    lateinit var sensorManager: SensorManager

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {

        linearX.text = event!!.values[0].toString()
        linearY.text =event!!.values[1].toString()
        linearZ.text = event!!.values[2].toString()

        //we aren't tracking *which* direction triggers the send, but i found it helpful to
        //better understand how

        if (event!!.values[0] > shakeThreshold ) {
            linearX.setTextColor(Color.parseColor("#0aad3f"))
        }
        if (event!!.values[1] > shakeThreshold) {
            linearY.setTextColor(Color.parseColor("#0aad3f"))
        }
        if (event!!.values[2] > shakeThreshold) {
            linearZ.setTextColor(Color.parseColor("#0aad3f"))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_page)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        launchButton.setOnClickListener{
            driverTest()
        }
    }

    private fun driverTest(){

        val driverName = "Bailey"

        val reactTime = .1243

        val dateTime = Date()

        val ref = FirebaseDatabase.getInstance().getReference("Drivers")

        val dataID = ref.push().key


        val driver = DriverModel(dataID, driverName, reactTime, dateTime)

        //error occurred needed !! on dataID because of string, string ? mismatch
        ref.child(dataID!!).setValue(driver).addOnCompleteListener{
            Toast.makeText(applicationContext, "Driver Saved", Toast.LENGTH_LONG).show()
            writeToLocalFile(driverName, reactTime)
        }
    }

    private fun writeToLocalFile(driverName: String, reactTime: Double){
        try{
            val fileName = "driverText.txt"
            val content = driverMessage(driverName, reactTime)
            applicationContext.openFileOutput(fileName, Context.MODE_PRIVATE).use{
                it.write(content.toByteArray())
            }
            /*val textFromLocal: String = File(file).bufferedWriter().use{
                out -> out.write(driverMessage(driverName, reactTime))
            }*/
        }
        catch(e: Exception){

        }
    }

    private fun driverMessage(name: String, time: Double): String{
        val driverTextChain = StringBuilder()
        driverTextChain.append("Hi ").append(name).append(',').append('\n')
        driverTextChain.append("Here is your response time: ").append(time).append(" seconds.")
        return driverTextChain.toString()
    }


}