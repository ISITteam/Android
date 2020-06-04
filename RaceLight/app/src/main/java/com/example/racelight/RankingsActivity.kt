package com.example.racelight

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.rankings_page.*

class RankingsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rankings_page)


        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity :: class.java))


        }
    }

}