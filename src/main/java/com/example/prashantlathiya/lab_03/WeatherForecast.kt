package com.example.prashantlathiya.lab_03

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class WeatherForecast : Activity() {
    lateinit var windSpeed:TextView
    lateinit var minimumTemp:TextView
    lateinit var maximumTemp:TextView
    lateinit var currentTemp:TextView

    lateinit var progressBar:ProgressBar
    lateinit var img:ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_forecast)
        img = findViewById<ImageView>(R.id.currentWeatherImage)
        currentTemp = findViewById<TextView>(R.id.currentTWeatherTemperature)
        minimumTemp = findViewById<TextView>(R.id.minimumTemperature)
        maximumTemp = findViewById<TextView>(R.id.maximumTemperature)
        windSpeed = findViewById<TextView>(R.id.windSpeed)
        progressBar = findViewById<ProgressBar>(R.id.progress)

        progressBar.visibility = View.VISIBLE

        var myQuery = ForecastQuery()

        myQuery.execute()
    }

    inner class ForecastQuery : AsyncTask<String, Integer, String>() {

        var progress = 0;
        var currtemperature: String? = null
        var minemperature: String? = null
        var maxtemperature: String? = null
        var iconName: String? = null
        lateinit var weatherURL: String
        var speed: String? = null
        var bitmp: Bitmap? = null
        fun fileExistance(fname: String): Boolean {
            val file = getBaseContext().getFileStreamPath(fname)
            return file.exists()
        }

        override fun doInBackground(vararg p0: String?): String {

            val url = URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric")
            var connection = url.openConnection() as HttpURLConnection
            var response = connection.inputStream


            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = false
            val xpp = factory.newPullParser()
            xpp.setInput(response, "UTF-8")

            while (xpp.eventType != XmlPullParser.END_DOCUMENT) {
                when (xpp.eventType) {
                    XmlPullParser.START_TAG -> {
                        if (xpp.name.equals("speed")) {
                            speed = xpp.getAttributeValue(null, "value")
                            progress += 20
                        } else if (xpp.name.equals("temperature")) {
                            currtemperature = xpp.getAttributeValue(null, "value")
                            minemperature = xpp.getAttributeValue(null, "min")
                            maxtemperature = xpp.getAttributeValue(null, "max")
                            progress += 60
                        }
                        if (xpp.name.equals("weather")) {
                            iconName = xpp.getAttributeValue(null, "icon")
                            var filename = "$iconName.png"
                            if (fileExistance(filename)) {
                                var fileInputtstream: FileInputStream? = null
                                try {
                                    fileInputtstream = openFileInput(filename)
                                } catch (e: FileNotFoundException) {
                                    e.printStackTrace()
                                }
                                bitmp = BitmapFactory.decodeStream(fileInputtstream)


                            } else {
                                weatherURL = "http://openweathermap.org/img/w/$iconName.png"

                                bitmp = getImage(weatherURL)
                                val outputStream = openFileOutput(filename, Context.MODE_PRIVATE)
                                bitmp?.compress(Bitmap.CompressFormat.PNG, 80, outputStream)
                                outputStream.flush()
                                outputStream.close()
                            }
                            progress += 20
                        }
                        publishProgress()

                    }
                    XmlPullParser.TEXT -> {

                    }

                }
                xpp.next()
            }


            return "done"


        }

        override fun onPostExecute(result: String?) {
            img.setImageBitmap(bitmp)
            progressBar.visibility = View.INVISIBLE
        }

        override fun onProgressUpdate(vararg values: Integer?) {

            maximumTemp.setText("Max Temp= $maxtemperature")
            minimumTemp.setText("Min Temp=$minemperature")
            currentTemp.setText("Current TEmp=$currtemperature")
            windSpeed.setText("Wind Speed=$speed")
            progressBar.setProgress(progress)


        }

    }

    fun getImage(url: URL): Bitmap? {
        var connection: HttpURLConnection? = null
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val responseCode = connection.responseCode
            return if (responseCode == 200) {
                BitmapFactory.decodeStream(connection.inputStream)
            } else
                null
        } catch (e: Exception) {
            return null
        } finally {
            connection?.disconnect()
        }
    }

    fun getImage(urlString: String): Bitmap? {
        try {
            val url = URL(urlString)
            return getImage(url)
        } catch (e: MalformedURLException) {
            return null
        }

    }
}
