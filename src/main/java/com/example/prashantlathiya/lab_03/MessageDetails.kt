package com.example.prashantlathiya.lab_03

import android.app.Activity
import android.os.Bundle

class MessageDetails : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_details)
        //tablet verdsion of program

        var myInfo=intent.extras//get our bundle back
        var myMessageFragment=MessageFragment()

        myMessageFragment.arguments=myInfo

        myMessageFragment.onTablet=false
        var loadFragment=getFragmentManager().beginTransaction()//this is a fragment transaction
        loadFragment.replace(R.id.Fragment_location,myMessageFragment)

        loadFragment.commit()
    }
}
