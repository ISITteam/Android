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

        infoButton.setOnClickListener {
            startActivity(Intent(this, InstructionActivity::class.java))
        }


        startButton.setOnClickListener {
            startActivity(Intent(this, StartActivity :: class.java))


        }

        rankingsButton.setOnClickListener {
            startActivity(Intent(this, RankingsActivity :: class.java))

        }

    }


}
