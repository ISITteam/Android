package com.example.racelight

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        startButton.setOnClickListener {
            startActivity(Intent(this, StartActivity :: class.java))



        }

        rankingsButton.setOnClickListener {
            startActivity(Intent(this, RankingsActivity :: class.java))

        }

    }


}
