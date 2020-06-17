package com.example.racelight

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.racelight.R.layout.start_page
import com.example.racelight.R.string.cancel
import com.example.racelight.R.string.save
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.start_page.*
import kotlinx.android.synthetic.main.username.*
import java.util.*
import kotlin.properties.Delegates

import android.graphics.Color
import android.widget.ImageView
import kotlinx.android.synthetic.main.start_page.*
import java.util.*


class StartActivity : AppCompatActivity(), SensorEventListener,
    UsernameDialogFragment.UsernameInterfaceListener {
    lateinit var countdownThree: ImageView
    lateinit var countdownTwo: ImageView
    lateinit var countdownOne: ImageView
    lateinit var countdownGo: ImageView

    //10 is a pretty vigorous shake, 5 is a little softer than i think i'd like,  3 might still randomly trigger.
    private var shakeThreshold = 7;

    private var clickTime by Delegates.notNull<Long>();
    lateinit var sensorManager: SensorManager
    var countdown: Boolean = true;
    var usernameToSave: String = "test";
    var difference by Delegates.notNull<Long>();
    lateinit var reactionTime: String;
    var success: Boolean = false;
    var sensorSwitch: Boolean = false

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        Countdown.visibility = View.VISIBLE
        if (kotlin.math.abs(event!!.values[0]) > shakeThreshold || kotlin.math.abs(event!!.values[1]) > shakeThreshold || kotlin.math.abs(
                event!!.values[2]
            ) > shakeThreshold
        ) {
            /* need a flag to not keep changing the sensor */

            if (!countdown) {
                if (!sensorSwitch) {
                    val sensorTime = System.currentTimeMillis()
                    difference = sensorTime - clickTime /*reaction time in milliseconds*/
                    val remainder = difference % 1000
                    val seconds = difference / 1000
                    reactionTime = seconds.toString() + "." + remainder.toString()
                    Countdown.text = "Reaction Time: " + reactionTime + " seconds"
                }
                sensorSwitch = true;
                sendButton.visibility = View.VISIBLE
                sendButton.isClickable = true;
                //comment the two lines below to disable the Launch button
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

        countdownThree = findViewById<ImageView>(R.id.three)
        countdownTwo = findViewById<ImageView>(R.id.two)
        countdownOne = findViewById<ImageView>(R.id.one)
        countdownGo = findViewById<ImageView>(R.id.go)

        launchButton.setOnClickListener {
            startAnimation()
        }

        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))


        }



        sendButton.setOnClickListener(View.OnClickListener {

            val newFragment = UsernameDialogFragment()
            newFragment.show(supportFragmentManager, "username")


        })
    }

    private fun startAnimation(){
        sensorSwitch = false;
        launchButton.isClickable = false
        launchButton.visibility = View.INVISIBLE
        sendButton.isClickable = false
        sendButton.visibility = View.INVISIBLE

        Countdown.visibility = View.INVISIBLE

        countdownThree.alpha = 1f
        countdownThree.scaleX = 1f
        countdownThree.scaleY = 1f
        countdownTwo.scaleX = 1f
        countdownTwo.scaleY = 1f
        countdownOne.scaleX = 1f
        countdownOne.scaleY = 1f
        countdownGo.scaleX = 1f
        countdownGo.scaleY = 1f

        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, .1f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, .1f)

        val goX = PropertyValuesHolder.ofFloat(View.SCALE_X, 2f)
        val goY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 2f)

        val a3One = ObjectAnimator.ofPropertyValuesHolder(countdownThree, scaleX, scaleY)
        a3One.setDuration(700)
        //animator.start()
        val a3Two = ObjectAnimator.ofFloat(countdownThree, View.ALPHA, 0f)
        a3Two.setDuration(200)
        val a3Three = ObjectAnimator.ofFloat(countdownTwo, View.ALPHA, 1f)
        a3Three.setDuration(50)

        val a2One = ObjectAnimator.ofPropertyValuesHolder(countdownTwo, scaleX, scaleY)
        a2One.setDuration(700)
        val a2Two = ObjectAnimator.ofFloat(countdownTwo, View.ALPHA, 0f)
        a2Two.setDuration(200)
        val a2Three = ObjectAnimator.ofFloat(countdownOne, View.ALPHA, 1f)
        a3Three.setDuration(50)

        val a1One = ObjectAnimator.ofPropertyValuesHolder(countdownOne, scaleX, scaleY)
        a1One.setDuration(700)
        val a1Two = ObjectAnimator.ofFloat(countdownOne, View.ALPHA, 0f)
        a1Two.setDuration(200)
        val a1Three = ObjectAnimator.ofFloat(countdownGo, View.ALPHA, 1f)
        a1Three.setDuration(50)

        val go = ObjectAnimator.ofPropertyValuesHolder(countdownGo, goX, goY)
        go.setDuration(50)
        val end = ObjectAnimator.ofFloat(countdownGo, View.ALPHA, 0f)

        val animatorMaster = AnimatorSet()
        animatorMaster.playSequentially(a3One, a3Two, a3Three, a2One, a2Two, a2Three, a1One, a1Two, a1Three, go, end)
        animatorMaster.start()

        /*countdown goes: 3,2,1,GO!*/
        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //Countdown.text = "seconds remaining: " + ((millisUntilFinished / 1000) + 1)
            }

            override fun onFinish() {
                //Countdown.text = "GO!"
                clickTime = System.currentTimeMillis()
                countdown = false
            }

        }.start()
    }



    override fun onDialogPositiveClick(dialog: DialogFragment, newName: String) {
        //Countdown.text = newName
        //difference is in milliseconds
        //seconds is a string.. intended for float?, 'second.miliseconds'
        var success: Boolean = driverTest(newName, difference)

        if (success) {

            intent = Intent(this, RankingsActivity::class.java)
            startActivity(intent)
        } else {
            Countdown.text = "oops. Something went wrong"
        }

    }


    override fun onDialogNegativeClick(dialog: DialogFragment, newName: String) {

    }

    //returns true if successful
    private fun driverTest(name: String, reactTime: Long): Boolean {


        val dateTime = Date()

        val ref = FirebaseDatabase.getInstance().getReference("Drivers")

        val dataID = ref.push().key


        val driver = DriverModel(dataID, name, reactTime, dateTime)

        //error occurred needed !! on dataID because of string, string ? mismatch
        //applicationContext is causing errors


/*
* need a way to confirm a successful firebase post; Return true if it works, false if it doesn't
* */
        /* Wait for Firebase call.  */
        ref.child(dataID!!).setValue(driver).addOnCompleteListener {
            //some kind of flag here.
            writeToLocalFile(name, reactTime)
        }
        return true;

    }

    private fun writeToLocalFile(driverName: String, reactTime: Long) {
        try {
            val fileName = "driverText.txt"
            val content = driverMessage(driverName, reactTime)
            applicationContext.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                it.write(content.toByteArray())
            }
        } catch (e: Exception) {

        }
    }

    private fun driverMessage(name: String, time: Long): String {
        val seconds = time / 1000
        val miliseconds = time % 1000
        val reactionTime = "${seconds.toString() + "." + miliseconds.toString()}"
        val driverTextChain = StringBuilder()
        driverTextChain.append("Hi ").append(name).append(',').append('\n')
        driverTextChain.append("Here is your response time: ").append(reactionTime).append(" seconds.")
        return driverTextChain.toString()
    }



}

class UsernameDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(com.example.racelight.R.layout.username, null))
                // Add action buttons
                .setPositiveButton(
                    save,
                    DialogInterface.OnClickListener { dialog, id ->

                        listener.onDialogPositiveClick(
                            this,
                            this.dialog?.newUsername?.text.toString()
                        )
                    })
                .setNegativeButton(cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onDialogNegativeClick(
                            this,
                            this.dialog?.newUsername?.text.toString()
                        )
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    // Use this instance of the interface to deliver action events
    internal lateinit var listener: UsernameInterfaceListener

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface UsernameInterfaceListener {

        fun onDialogPositiveClick(dialog: DialogFragment, newName: String)
        fun onDialogNegativeClick(dialog: DialogFragment, newName: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as UsernameInterfaceListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        " must implement NoticeDialogListener")
            )
        }
    }

}




