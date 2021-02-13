package com.example.tp2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.concurrent.thread
import kotlin.math.log


class MainActivity : AppCompatActivity() {

    private var locationGps: Location? = null
    private var locationNetwork: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getLocation()
        //get input and execute a new thread of function login
        loginButton.setOnClickListener {
            var usernameStr = username.text.toString()
            var passwordStr = password.text.toString()
            thread { login(usernameStr, passwordStr) }
        }

        //jump to  activity2
        jumpButton.setOnClickListener {
            val intent = Intent(this, Actitivy2::class.java)
            intent.putExtra("imageName", imageName.text)
            startActivity(intent)
        }
        //jump to  activity3
        jumpButton2.setOnClickListener {
            val intent = Intent(this, Activity3::class.java)
            intent.putExtra("imageName", imageName.text)
            startActivity(intent)
        }



    }


    /**
     * login function
     *
     * @return void
     */
    fun login(username: String, password: String) {
        var url: URL? = null
        try {
            url = URL("https://httpbin.org/basic-auth/bob/sympa")
            val basicAuth = "Basic " + Base64.encodeToString(
                "${username}:${password}".toByteArray(),
                Base64.NO_WRAP
            )
            val urlConnection: HttpsURLConnection = url.openConnection() as HttpsURLConnection
            urlConnection.setRequestProperty("Authorization", basicAuth)
            try {
                val data: InputStream = BufferedInputStream(urlConnection.inputStream)
                val s = data.bufferedReader().readText()
                val json = JSONObject(s)
                thread {
                    runOnUiThread {
                        textView2.text = json["user"].toString()
                    }
                }

            } finally {
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    /**
     * get current location info and get image info
     */
    @SuppressLint("MissingPermission")
    fun getLocation() {
        val locaiton:Location


        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        //get location through gps or network
        if (hasGps || hasNetwork) {
            if (hasGps) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F,
                    object : android.location.LocationListener {

                        override fun onLocationChanged(location: Location) {
                            "lat:${location.latitude.toString()},lon:${location.longitude.toString()} "
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {

                        }

                    }
                )
                val localGpsLocation =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null)
                    locationGps = localGpsLocation

            }
            if (hasNetwork) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F,
                    object : android.location.LocationListener {

                        override fun onLocationChanged(location: Location) {
                            "lat:${location.latitude.toString()},lon:${location.longitude.toString()} "
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {

                        }

                    }
                )

                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null)
                    locationNetwork = localNetworkLocation
            }
            if (locationGps == null && locationNetwork == null){
                Log.e("locaton error","can't get location, please check your configuration")
                return
            }
            if (locationGps != null && locationNetwork != null){
                locaiton = if (locationGps!!.accuracy > locationNetwork!!.accuracy) locationGps!! else locationNetwork!!
            }else{
                locaiton = locationGps ?: locationNetwork!!
            }

            // get altitude and longitude
            val lat = locaiton.altitude
            val lon = locaiton.longitude

            thread { //get images by locaiton
                try {
                    var url = URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&license=4&api_key=ec418654e2fd4808f268bdfadb63ccd2&has_geo=1&lat=${lat}&lon=${lon}&per_page=1&format=json")
                    val urlConnection: HttpsURLConnection = url.openConnection() as HttpsURLConnection
                    try {
                        val data: InputStream = BufferedInputStream(urlConnection.inputStream)
                        val s = data.bufferedReader().readText()

                        val json = JSONObject(s.substring(s.indexOf('{'),s.lastIndex))
                        Log.i("imagesInfo",json.toString())
                    } finally {
                        urlConnection.disconnect()
                    }
                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } }


        }
    }
}