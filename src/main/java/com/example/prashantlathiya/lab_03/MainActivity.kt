package com.example.prashantlathiya.lab_03

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : Activity() {
    val ACTIVITY_NAME = "StartActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(ACTIVITY_NAME, "In onCreate()")
        var loginbutton = findViewById<Button>(R.id.button)
        var chatButton = findViewById<Button>(R.id.button2)
        var viewTemperature=findViewById<Button>(R.id.viewTemperature)
        loginbutton?.setOnClickListener({
            val newActivity = Intent( this, ListItemsAcitivity::class.java)

            startActivityForResult(newActivity,50)



        })

        chatButton.setOnClickListener({
            var intent =Intent(this,ChatWindow::class.java)
            startActivityForResult(intent,50)
        })

        viewTemperature.setOnClickListener({
            var intent=Intent(this, WeatherForecast::class.java)
            startActivityForResult(intent,50)
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == 50 && resultCode == 1)
        {
            val messagePassed = data?.getStringExtra("Response")
            var duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(this , messagePassed.toString(), duration) //this is the ListActivity
            toast.show() //display your message box

        }
    }

    override fun onResume() {
        super.onResume()

        val ACTIVITY_NAME = "StartActivity"

        Log.i(ACTIVITY_NAME, "In onResume()")
    }

    override fun onStart() {
        super.onStart()

        val ACTIVITY_NAME = "StartActivity"

        Log.i(ACTIVITY_NAME, "In onStart()")
    }

    override fun onPause() {
        super.onPause()
        val ACTIVITY_NAME = "StartActivity"

        Log.i(ACTIVITY_NAME, "In onPause()")
    }

    override fun onStop() {
        super.onStop()

        val ACTIVITY_NAME = "StartActivity"

        Log.i(ACTIVITY_NAME, "In onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()

        val ACTIVITY_NAME = "StartActivity"

        Log.i(ACTIVITY_NAME, "In onDestroy()")
    }
}
