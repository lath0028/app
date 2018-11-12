package com.example.prashantlathiya.lab_03

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

class LoginActivity : Activity() {
    val ACTIVITY_NAME = "StartActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPref = this.getSharedPreferences("DataFile", Context.MODE_PRIVATE) ?: return
        val editText = findViewById<EditText>(R.id.editText)
        editText.setText(sharedPref.getString("loginName", "email@domain.com"))
        var btnLogin = findViewById(R.id.button) as? Button
        btnLogin?.setOnClickListener( { btnLoginClick() })

    }

    override fun onResume() {
        super.onResume()
        Log.i(ACTIVITY_NAME, "In onResume()")
    }

    override fun onStart() {
        super.onStart()
        Log.i(ACTIVITY_NAME, "In onStart()")
    }

    override fun onPause() {
        super.onPause()
        Log.i(ACTIVITY_NAME, "In onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.i(ACTIVITY_NAME, "In onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(ACTIVITY_NAME, "In onDestroy()")
    }

    fun btnLoginClick() {

        val editText = findViewById<EditText>(R.id.editText)

        val loginName = editText.text.toString()

        val sharedPref = getSharedPreferences("DataFile", Context.MODE_PRIVATE)

        val prefs = sharedPref.edit()

        prefs.putString("loginName",loginName)

        prefs.commit()


        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
    }
}
