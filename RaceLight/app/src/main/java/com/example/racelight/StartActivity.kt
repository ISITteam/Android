package com.example.racelight

import android.R
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.start_page.*
import com.example.racelight.R.layout.start_page
import java.util.*
import kotlin.properties.Delegates


class StartActivity: AppCompatActivity(), SensorEventListener {
    //10 is a pretty vigorous shake, 5 is a little softer than i think i'd like,  3 might still randomly trigger.
    private var shakeThreshold = 7;

   private var clickTime by Delegates.notNull<Long>();
    lateinit var sensorManager: SensorManager
    var countdown:Boolean = true;


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {


        if (kotlin.math.abs(event!!.values[0]) > shakeThreshold || kotlin.math.abs(event!!.values[1]) > shakeThreshold || kotlin.math.abs(event!!.values[2]) > shakeThreshold) {


            if(!countdown){

            val  sensorTime = System.currentTimeMillis()
             val difference:Long =  sensorTime - clickTime /*reaction time in milliseconds*/
                val remainder = difference %1000
                val seconds = difference /1000
                val reactionTime:String = "$seconds.$remainder"
                Countdown.text = "Reaction Time:" + reactionTime + " seconds"

                sendButton.visibility = View.VISIBLE
                launchButton.visibility = View.VISIBLE
                launchButton.isClickable = true;

            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(start_page)
                sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        launchButton.setOnClickListener{
            launchButton.isClickable = false
            launchButton.visibility = View.INVISIBLE
            Countdown.visibility = View.VISIBLE
                                     /*countdown goes: 3,2,1,GO!*/
            object : CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    Countdown.text = "seconds remaining: " + ((millisUntilFinished / 1000) +1)
                }

                override fun onFinish() {
                    Countdown.text = "GO!"
                    clickTime = System.currentTimeMillis()
                    countdown = false
                    }

            }.start()
        }

        sendButton.setOnClickListener(View.OnClickListener { //instantiate the popup.xml layout file



    })
    }

    private fun driverTest(name: String,reactTime:Long ){



        val dateTime = Date()

        val ref = FirebaseDatabase.getInstance().getReference("Drivers")

        val dataID = ref.push().key


        val driver = DriverModel(dataID, name, reactTime, dateTime)

        //error occurred needed !! on dataID because of string, string ? mismatch
        ref.child(dataID!!).setValue(driver).addOnCompleteListener{
            Toast.makeText(applicationContext, "Driver Saved", Toast.LENGTH_LONG).show()
        }
    }


}