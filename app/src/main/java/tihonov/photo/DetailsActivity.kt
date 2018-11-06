package tihonov.photo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_detail.*
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.URL

class DetailsActivity : AppCompatActivity() {

    private lateinit var downloadTask: DownloadPostsAsyncTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        getIncomingIntent()
    }

    private fun getIncomingIntent() {
        if (intent.hasExtra("image_url") && intent.hasExtra("user_name")) {
            val name = findViewById<TextView>(R.id.image_description)
            name.text = intent.getStringExtra("user_name")

            downloadTask = DownloadPostsAsyncTask(WeakReference(this))
            downloadTask.execute(URL(intent.getStringExtra("image_url")))
        }
    }

    private class DownloadPostsAsyncTask(val activity: WeakReference<DetailsActivity>) : AsyncTask<URL, Int, Bitmap>() {
        override fun onPostExecute(result: Bitmap?) {
            activity.get()?.image?.setImageBitmap(result)
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

            return BitmapFactory.decodeByteArray(res, 0, res!!.size)
        }
    }
}
