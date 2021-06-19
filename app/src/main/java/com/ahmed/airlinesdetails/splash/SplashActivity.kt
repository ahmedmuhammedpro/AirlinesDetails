package com.ahmed.airlinesdetails.splash

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.ahmed.airlinesdetails.R
import com.ahmed.airlinesdetails.main_view.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val flyImage: ImageView = findViewById(R.id.airplane_image)
        val flyInAnimation = AnimationUtils.loadAnimation(this, R.anim.fly_in)
        flyImage.animation = flyInAnimation
        flyInAnimation.start()
        flyInAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                Intent(this@SplashActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(this)
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {
                val x = 1
            }

        })
    }
}