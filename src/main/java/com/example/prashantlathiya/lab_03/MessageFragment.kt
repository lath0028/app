package com.example.prashantlathiya.lab_03


import android.app.Activity
import android.os.Bundle
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView


/**
 * A simple [Fragment] subclass.
 *
 */
class MessageFragment : Fragment() {

      var parent:ChatWindow?=null
var onTablet=false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        // Inflate the layout for this fragment
        var dataPassed=arguments//get your data back
        var string =dataPassed.getString("Message")
        var id=dataPassed.getLong("ID")


        var screen=inflater.inflate(R.layout.fragment_message, container, false)
        var textView=screen.findViewById<TextView>(R.id.messageText)
textView.setText(string)

        var idView=screen.findViewById<TextView>(R.id.id_text)
        idView.setText(id.toString())

        var delButton=screen.findViewById<Button>(R.id.deleteButton)

        delButton.setOnClickListener {
            if(onTablet) {
                parent!!.deleteMessage(id)
                (parent!!).fragmentManager.beginTransaction().remove(this).commit()//remove from screen
            }
            else{
                //phone
                var dataBack=Intent()
                dataBack.putExtra("ID",id)
                activity.setResult(Activity.RESULT_OK,dataBack)
                activity.finish()
            }

        }
        return screen
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(onTablet) {
            parent = context as ChatWindow//need parent for later removing fragment
        }

    }

}
