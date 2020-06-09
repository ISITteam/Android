package com.example.racelight

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_animate.*

class AnimateActivity : AppCompatActivity() {
    lateinit var countdownThree: ImageView
    lateinit var countdownTwo: ImageView
    lateinit var countdownOne: ImageView
    lateinit var countdownGo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animate)
        countdownThree = findViewById<ImageView>(R.id.three)
        countdownTwo = findViewById<ImageView>(R.id.two)
        countdownOne = findViewById<ImageView>(R.id.one)
        countdownGo = findViewById<ImageView>(R.id.go)
        loadButton.setOnClickListener { animateCountDown() }
    }

    private fun animateCountDown(){
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, .1f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, .1f)

        val goX = PropertyValuesHolder.ofFloat(View.SCALE_X, 2f)
        val goY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 2f)

        val a3One = ObjectAnimator.ofPropertyValuesHolder(countdownThree, scaleX, scaleY)
        a3One.setDuration(500)
        //animator.start()
        val a3Two = ObjectAnimator.ofFloat(countdownThree, View.ALPHA, 0f)
        a3Two.setDuration(100)
        val a3Three = ObjectAnimator.ofFloat(countdownTwo, View.ALPHA, 1f)
        a3Three.setDuration(20)

        val a2One = ObjectAnimator.ofPropertyValuesHolder(countdownTwo, scaleX, scaleY)
        a2One.setDuration(500)
        val a2Two = ObjectAnimator.ofFloat(countdownTwo, View.ALPHA, 0f)
        a2Two.setDuration(100)
        val a2Three = ObjectAnimator.ofFloat(countdownOne, View.ALPHA, 1f)
        a3Three.setDuration(20)

        val a1One = ObjectAnimator.ofPropertyValuesHolder(countdownOne, scaleX, scaleY)
        a1One.setDuration(500)
        val a1Two = ObjectAnimator.ofFloat(countdownOne, View.ALPHA, 0f)
        a1Two.setDuration(100)
        val a1Three = ObjectAnimator.ofFloat(countdownGo, View.ALPHA, 1f)
        a1Three.setDuration(20)

        val go = ObjectAnimator.ofPropertyValuesHolder(countdownGo, goX, goY)
        go.setDuration(50)

        val animatorMaster = AnimatorSet()
        animatorMaster.playSequentially(a3One, a3Two, a3Three, a2One, a2Two, a2Three, a1One, a1Two, a1Three, go)
        animatorMaster.start()
    }
}
