package tihonov.photo

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_detail.*
import java.io.File
import java.io.FileOutputStream

class DetailsActivity : AppCompatActivity() {

    private var urlStr = ""
    private var bind = false
    private lateinit var file: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (intent.hasExtra("image_url") && intent.hasExtra("user_name")) {
            val name = findViewById<TextView>(R.id.image_description)
            name.text = intent.getStringExtra("user_name")
            urlStr = intent.getStringExtra("image_url")

            file = File(filesDir, urlStr.hashCode().toString() + ".jpg")

            if (file.exists()) {
                myImage.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
            } else {
                val intent = Intent(this, PhotoLoader::class.java)
                intent.putExtra(PHOTO_URL, urlStr)
                bindService(intent, serviceConnection, BIND_AUTO_CREATE)
                startService(intent)
            }
        }
    }

    private var binder: PhotoLoader.MyBinder? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            bind = true
            binder = service as PhotoLoader.MyBinder

            binder!!.setCallback { p ->
                myImage.setImageBitmap(p)
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            bind = false
            binder = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (bind) {
            bind = false
            unbindService(serviceConnection)
        }
    }

    companion object {
        val PHOTO_URL = DetailsActivity::class.java.name + ".photoUrl"
    }
}
