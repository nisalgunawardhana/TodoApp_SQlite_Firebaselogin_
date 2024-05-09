package com.example.todo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val delayTimeMs: Long = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            CoroutineScope(Dispatchers.Main).launch {
                delay(delayTimeMs)

                // Perform the navigation here after the delay
                val intent = Intent(this@MainActivity, MainActivity2::class.java)
                startActivity(intent)

                // Finish the current activity if you don't want it to be in the back stack
                finish()
            }
        }
    }