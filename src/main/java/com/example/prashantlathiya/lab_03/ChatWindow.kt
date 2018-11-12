package com.example.prashantlathiya.lab_03

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_chat_window.*
import kotlinx.android.synthetic.main.activity_login.*

class ChatWindow : Activity() {
    val message = "Hi"
    var numItems = 100

    var messageClicked=0
    val arrayList = ArrayList<String>()
    lateinit var messageAdapter:MyAdapter
    val DATABASE_NAME = "Messages.db"
    val VERSION_NUM = 6
lateinit var results:Cursor

   lateinit var db: SQLiteDatabase
    lateinit var dbChatDatabase_Helper: ChatDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_window)

        dbChatDatabase_Helper = ChatDatabaseHelper()//get a helper object
        db = dbChatDatabase_Helper.writableDatabase //open your database

        val frgamentLocation=findViewById<FrameLayout>(R.id.Fragment_location)
        var onTablet=(frgamentLocation!=null)




        var myList = findViewById<ListView>(R.id.myList)
        var myInfo=Bundle()


        myList.setOnItemClickListener { parent, view, position, id ->
            myInfo.putString("Message",arrayList.get(position))
            myInfo.putLong("ID",id)

            messageClicked=position
            if(onTablet) {
                //tablet verdsion of program
                var myMessageFragment=MessageFragment()

                myMessageFragment.parent=this
                var loadFragment=getFragmentManager().beginTransaction()//this is a fragment transaction
                loadFragment.replace(R.id.Fragment_location,myMessageFragment)
                myMessageFragment.arguments=myInfo
                myMessageFragment.onTablet=false
                loadFragment.commit()
            }
            else
            {

                var detailActivity=Intent(this,MessageDetails::class.java)
                detailActivity.putExtras(myInfo)//send data to next page
                startActivityForResult(detailActivity,35)

                //phone versionof prepare
            }
        }

messageAdapter=MyAdapter(this)
        myList.adapter = messageAdapter

        //ar myList=findViewById<ListView>(R.id.myList)

        val inputText = findViewById<EditText>(R.id.editText3)
        val sendButton = findViewById<Button>(R.id.sendButton)

        // var theAdapter = MyAdapter()

        sendButton.setOnClickListener({
            val userTyped = inputText.text.toString()
            arrayList.add(userTyped)
            messageAdapter.notifyDataSetChanged()
            //write a database
            val newRow = ContentValues()
            newRow.put(dbChatDatabase_Helper.KEY_MESSGAES, userTyped)
            db.insert(dbChatDatabase_Helper.TABLE_NAME, "", newRow)

            results = db.query(dbChatDatabase_Helper.TABLE_NAME, arrayOf("_id", dbChatDatabase_Helper.KEY_MESSGAES), null, null, null, null, null, null)

            inputText.setText(" ")
        })

         results = db.query(dbChatDatabase_Helper.TABLE_NAME, arrayOf("_id", dbChatDatabase_Helper.KEY_MESSGAES), null, null, null, null, null, null)
        val numrows = results.getCount()
        val numColumns=results.getColumnCount()
        //results.getColumnName(0)
        //Cursor cursor = db.rawQuery(...);
//       while (results.moveToNext())
//       {}

        for(c in 0..numColumns-1)
        {
            Log.i("column names:",results.getColumnName(c));

        }


        results.moveToFirst()

        val idIndex = results.getColumnIndex("_id")//find index of the _id column
        val messageIndex = results.getColumnIndex(dbChatDatabase_Helper.KEY_MESSGAES)//find the id of message column


        while (!results.isAfterLast)//while you're not done reading data
        {
            var thisID = results.getInt(idIndex)
            var thisMessage = results.getString(messageIndex)
            arrayList.add(thisMessage)
            results.moveToNext()//look at next row in table
        }

//        // var theAdapter = ArrayAdapter<String>(this , R.layout.list_cell, arrayOf("A","B", "C", "d", "e"))
//        myList.setAdapter(theAdapter)
//        myList.setOnItemClickListener(AdapterView.OnItemClickListener{ lv, vw, pos, id ->
//
//            Log.i("You clicked" , " number " + pos)
//
//        })
    }

    inner class MyAdapter(context: Context) : ArrayAdapter<String>(context, 0) {

        override fun getCount(): Int { //how many items
            return arrayList.size
        }

        override fun getItem(position: Int): String { //what to show
            return arrayList.get(position)
        }

        override fun getItemId(position: Int): Long {// what is the ID of item at position

            results.moveToPosition(position)
            return results.getLong(results.getColumnIndex("_id")).toLong() as Long

        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

            var inflater = LayoutInflater.from(parent.context)
            var result = null as View?

            if (position % 2 == 0)

                result = inflater.inflate(R.layout.chat_row_incoming, null)
            else

                result = inflater.inflate(R.layout.chat_row_outgoing, null)
////            var thisRow = convertView
////            val inflater = LayoutInflater.from(parent.getContext())
////
////            if(thisRow == null)
////                thisRow = inflater.inflate(R.layout.row_layout, null) //this loads row_layout.xml //allocate memory
////            var thisBox = thisRow?.findViewById<CheckBox>(R.id.cbox) //this is the check bo
////            var thisText = thisRow?.findViewById<TextView>(R.id.textView) //this is the text
////
////            if (position < 5)
////                thisBox?.isChecked = true
////            else
////                thisBox?.isChecked = false
////
////            thisText?.setText(getItem(position))
////
////            return thisRow

            val message = result?.findViewById<TextView>(R.id.messageText)

            message?.text = getItem(position) // get the string at position
            // textView.setText(getItem(position))

            return result
        }


    }


    val TABLE_NAME = "ChatMessages"

    inner class ChatDatabaseHelper : SQLiteOpenHelper(this@ChatWindow, DATABASE_NAME, null, VERSION_NUM) {
        val TABLE_NAME = "ChatMessages"
        val KEY_MESSGAES = "Messages"


        override fun onCreate(db: SQLiteDatabase) {
            //db.execSQL("CREATE TABLE " + TABLE_NAME + "( _id INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MESSGAES + " TEXT)")
            db.execSQL("CREATE TABLE $TABLE_NAME ( _id INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_MESSGAES TEXT)")
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)


            //cerate new table

            onCreate(db)
        }

    }

    fun deleteMessage(id:Long)
    {
        //delete the message with id
        db.delete(TABLE_NAME,"_id=$id",null)

        results = db.query(dbChatDatabase_Helper.TABLE_NAME, arrayOf("_id", dbChatDatabase_Helper.KEY_MESSGAES), null, null, null, null, null, null)

        arrayList.removeAt(messageClicked)

        messageAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

                if((requestCode==35)&& (resultCode==Activity.RESULT_OK))
                {
                var num=(data!!.getLongExtra("ID",0))
                deleteMessage(num)
            }

    }

}


