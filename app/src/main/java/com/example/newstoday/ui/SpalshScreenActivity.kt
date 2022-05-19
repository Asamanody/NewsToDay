package com.example.newstoday.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.newstoday.R

class SpalshScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh_screen)



        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this,NewsActivity::class.java)
            startActivity(intent)
            this.finish()
        }, 3000)
    }
}