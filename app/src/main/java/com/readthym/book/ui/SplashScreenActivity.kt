package com.readthym.book.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.readthym.book.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.*

class SplashScreenActivity : AppCompatActivity() {

    val binding by lazy { ActivitySplashScreenBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        GlobalScope.launch {
            delay(3000)
            withContext(Dispatchers.Main) {
                dsf()
            }
        }

    }

    private fun dsf() {
        finish()
        startActivity(Intent(this, NavdrawContainerActivity::class.java))
    }
}