package tihonov.photo

import android.app.IntentService
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.*
import tihonov.photo.DetailsActivity.Companion.PHOTO_URL
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class PhotoLoader : IntentService("PhotoLoader") {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var callback: (Bitmap) -> Unit

    override fun onHandleIntent(intent: Intent?) {
        val path = intent!!.getStringExtra(PHOTO_URL)
        val url = URL(path)
        url.openConnection()
        val bmp: Bitmap

        val file = File(filesDir, path.hashCode().toString() + ".jpg")

        if (file.exists()) {
            bmp = BitmapFactory.decodeFile(file.absolutePath)
        } else {
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            file.createNewFile()
            val stream: FileOutputStream? = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream?.close()
        }

        handler.post {
            callback(bmp)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return MyBinder(this)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        callback = { }
        return super.onUnbind(intent)
    }

    class MyBinder(private val service: PhotoLoader) : Binder() {
        fun setCallback(callback: (Bitmap) -> Unit) {
            service.callback = callback
        }
    }
}
