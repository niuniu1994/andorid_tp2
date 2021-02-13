package com.example.tp2

import AsyncFlickrJSONData
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity2.*

/**
 * Activity who shows a downloaded image by clicking the button
 */
class Actitivy2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity2)
        val url = "https://www.flickr.com/services/feeds/photos_public.gne?tags=${intent.getStringExtra("imageName")}&format=json"
        //the button for getting image from a url
        buttonImage.setOnClickListener{

            //start a async mission to download information of image
            AsyncFlickrJSONData(this).execute(url)
        }
    }
}