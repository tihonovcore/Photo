package tihonov.photo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_gallery.*
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class GalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        Log.d(TAG, "onCreate: started.")

        getIncomingIntent()
    }

    private fun getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.")

        if (intent.hasExtra("image_url") && intent.hasExtra("image_name")) {
            Log.d(TAG, "getIncomingIntent: found intent extras.")

            val imageUrl = intent.getStringExtra("image_url")
            val imageName = intent.getStringExtra("image_name")

            setImage(imageUrl, imageName)
        }
    }

    private lateinit var downloadTask: DownloadPostsAsyncTask

    private fun setImage(imageUrl: String, imageName: String) {
        Log.d(TAG, "setImage: setting te image and name to widgets.")

        val name = findViewById<TextView>(R.id.image_description)
        name.text = imageName

        val image = findViewById<ImageView>(R.id.image)

        downloadTask = DownloadPostsAsyncTask(WeakReference(this))
        Log.d("IMAG", imageUrl)
        downloadTask.execute(URL(imageUrl))

        //image.setImageBitmap()
    }

    private class DownloadPostsAsyncTask(val activity: WeakReference<GalleryActivity>) : AsyncTask<URL, Int, Bitmap>() {
        override fun onPostExecute(result: Bitmap?) {
            activity.get()?.let {
                it.image.setImageBitmap(result)
            }
        }

        override fun doInBackground(vararg params: URL): Bitmap {
            var res: ByteArray?
            try {
                val url = params[0]
                var connection = url.openConnection()
                connection.connect()
                while (connection.contentLength < 0) {
                    connection = url.openConnection()
                    connection.connect()
                }
                res = ByteArray(connection.contentLength)
                connection.getInputStream().use { iss ->
                    var p = 0
                    var r: Int = iss.read(res, p, res!!.size - p)
                    while (r > 0){
                        p += r
                        r = iss.read(res, p, res!!.size - p)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                res = null
            }


            val response = BitmapFactory.decodeByteArray(res, 0, res!!.size)
            return response
        }
    }

    companion object {

        private val TAG = "GalleryActivity"
    }
}
