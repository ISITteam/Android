package com.example.racelight
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
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
import kotlinx.android.synthetic.main.start_page.*
import java.util.*


class StartActivity: AppCompatActivity(), SensorEventListener, UsernameDialogFragment.UsernameInterfaceListener {
    //10 is a pretty vigorous shake, 5 is a little softer than i think i'd like,  3 might still randomly trigger.
    private var shakeThreshold = 7;

    private var clickTime by Delegates.notNull<Long>();
    lateinit var sensorManager: SensorManager
    var countdown: Boolean = true;
    var usernameToSave: String = "test";
    var difference by Delegates.notNull<Long>();
    lateinit var reactionTime: String;


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {


        if (kotlin.math.abs(event!!.values[0]) > shakeThreshold || kotlin.math.abs(event!!.values[1]) > shakeThreshold || kotlin.math.abs(
                event!!.values[2]
            ) > shakeThreshold
        ) {


            if (!countdown) {

                val sensorTime = System.currentTimeMillis()
                difference = sensorTime - clickTime /*reaction time in milliseconds*/
                val remainder = difference % 1000
                val seconds = difference / 1000
                reactionTime = seconds.toString() + "." + remainder.toString()
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

        launchButton.setOnClickListener {
            launchButton.isClickable = false
            launchButton.visibility = View.INVISIBLE
            Countdown.visibility = View.VISIBLE
            /*countdown goes: 3,2,1,GO!*/
            object : CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    Countdown.text = "seconds remaining: " + ((millisUntilFinished / 1000) + 1)
                }

                override fun onFinish() {
                    Countdown.text = "GO!"
                    clickTime = System.currentTimeMillis()
                    countdown = false
                }

            }.start()
        }




        sendButton.setOnClickListener(View.OnClickListener {

            val newFragment = UsernameDialogFragment()
            newFragment.show(supportFragmentManager, "username")


        })
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, newName: String) {
        Countdown.text = newName
        //difference is in milliseconds
        //seconds is a float, 'second.miliseconds'
        driverTest(newName, difference)
        Countdown.text = "success"
        dialog.dismiss();
    }


    override fun onDialogNegativeClick(dialog: DialogFragment, newName: String) {

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
                        DialogInterface.OnClickListener { dialog ,id ->

                            listener.onDialogPositiveClick(this, this.dialog?.newUsername?.text.toString())
                        })
                    .setNegativeButton(cancel,
                        DialogInterface.OnClickListener { dialog, id ->
                            listener.onDialogNegativeClick(this, this.dialog?.newUsername?.text.toString())
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

            fun onDialogPositiveClick(dialog: DialogFragment, newName:String)
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
                throw ClassCastException((context.toString() +
                        " must implement NoticeDialogListener"))
            }
        }

    }

    private fun driverTest(name: String,reactTime:Long ){



        val dateTime = Date()

        val ref = FirebaseDatabase.getInstance().getReference("Drivers")

        val dataID = ref.push().key


        val driver = DriverModel(dataID, name, reactTime, dateTime)

        //error occurred needed !! on dataID because of string, string ? mismatch
      //applicationContext is causing errors

        ref.child(dataID!!).setValue(driver).addOnCompleteListener{
            /*Toast.makeText(
               applicationContext,
                "Driver Saved",
                Toast.LENGTH_LONG
            ).show()
*/

        }


    }


