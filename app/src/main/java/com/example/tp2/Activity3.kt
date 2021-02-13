package com.example.tp2

import AsyncFlickrJSONDataForList
import MyListAdapter
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.example.tp2.funs.ImageVo
import com.example.tp2.funs.MySingleton
import kotlinx.android.synthetic.main.activity3.*

/**
 * Activity who show a list of downloaded photos
 */
class Activity3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity3)
        val url = "https://www.flickr.com/services/feeds/photos_public.gne?tags=${intent.getStringExtra("imageName")}&format=json"
        AsyncFlickrJSONDataForList(this).execute(url)
    }

    /**
     * once get images info we put them in adapter
     */
     fun loadImages(images:MutableList<ImageVo>){
        val queue: RequestQueue = MySingleton.getInstance(this.applicationContext).requestQueue
        val myListAdapter = MyListAdapter(this, images,queue)
        listView.adapter = myListAdapter
    }
}