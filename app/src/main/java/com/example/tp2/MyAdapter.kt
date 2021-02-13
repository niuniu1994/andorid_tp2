import android.app.Activity
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.example.tp2.R
import com.example.tp2.funs.ImageVo
import com.example.tp2.funs.MySingleton
import kotlinx.android.synthetic.main.row.view.*

/**
 * adapter for loading info to list view
 */
class MyListAdapter(private val context: Activity, private val images: MutableList<ImageVo>,private val queue: RequestQueue)
    : ArrayAdapter<ImageVo>(context, R.layout.row, images) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.row, null, false)

        val titleText = rowView.title as TextView
        val imageView = rowView.icon as ImageView

        /**
         * download image using a queue and set them to imageView
         */
        queue.add(ImageRequest(images.get(position).imageUrl,
            Response.Listener<Bitmap> {imageView.setImageBitmap(it)},
            imageView.layoutParams.height,imageView.layoutParams.width,
            ImageView.ScaleType.CENTER,
            Bitmap.Config.ARGB_8888,Response.ErrorListener { Toast.makeText(context,"error",Toast.LENGTH_SHORT) }
            ))
        titleText.text = images.get(position).title

        return rowView
    }
}