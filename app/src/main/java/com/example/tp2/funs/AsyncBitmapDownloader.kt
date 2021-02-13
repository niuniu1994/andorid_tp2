package com.example.tp2.funs

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import kotlinx.android.synthetic.main.activity2.*
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection
/**
 * download image and set image to imageView
 */
class AsyncBitmapDownloader(content: Activity): AsyncTask<String, Void, Bitmap>(){
    val activity = content
    override fun doInBackground(vararg params: String?): Bitmap? {
        var bm: Bitmap? = null
        try {
            var url: URL? = URL(params[0])
            val urlConnection: HttpsURLConnection = url?.openConnection() as HttpsURLConnection
            try {
                val data: InputStream = BufferedInputStream(urlConnection.inputStream)
                bm = BitmapFactory.decodeStream(data)
            } finally {
                urlConnection.disconnect()
            }
        }catch (e: IOException){
            e.printStackTrace()
        }
        return bm
    }

    override fun onPostExecute(result: Bitmap) {
        super.onPostExecute(result)
        activity.imageView.setImageBitmap(result)
    }
}