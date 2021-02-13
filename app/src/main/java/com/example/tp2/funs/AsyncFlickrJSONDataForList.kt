import android.os.AsyncTask
import android.util.Log
import com.example.tp2.Activity3
import com.example.tp2.funs.ImageVo
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class AsyncFlickrJSONDataForList(context:Activity3) : AsyncTask<String, Void, JSONObject>() {
    val activity = context
    override fun doInBackground(vararg params: String): JSONObject? {

        var jsonRes: JSONObject? = null
        try {
            var url: URL? = URL(params[0])
            val urlConnection: HttpsURLConnection = url?.openConnection() as HttpsURLConnection
            try {
                val data: InputStream = BufferedInputStream(urlConnection.inputStream)
                var s:String = data.bufferedReader().readText()
                s = s.substring(s.indexOf('{'),s.lastIndex)
                jsonRes = JSONObject(s)
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return jsonRes;
    }



    override fun onPostExecute(result: JSONObject?) {
        super.onPostExecute(result)

        Log.i("tp2",result.toString())
        var imagesArr = mutableListOf<ImageVo>()
        val items:JSONArray = result!!.getJSONArray("items")
        for (index in 0..items.length() - 1){
            val imageUrl = result!!.getJSONArray("items").getJSONObject(index).getJSONObject("media").getString("m")
            val title = result!!.getJSONArray("items").getJSONObject(index).getString("title")
            val im = ImageVo(imageUrl,title)
            imagesArr.add(im)
        }
        activity.loadImages(imagesArr)

    }
}