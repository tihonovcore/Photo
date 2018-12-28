package tihonov.photo

import android.app.IntentService
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.Looper
import android.os.IBinder


class DBgetById : IntentService("getById") {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var callback: (FavPic?) -> Unit
    private lateinit var instance: Instance

    override fun onHandleIntent(intent: Intent) {
        instance = applicationContext as Instance

        val urlAddress = intent.getStringExtra("image_url")
        val picture = run {
            instance.favPicDao.getById(urlAddress.hashCode().toLong())
        }

        handler.post { callback(picture) }
    }

    override fun onBind(intent: Intent): IBinder? {
        return MyBinder(this)
    }

    class MyBinder(private val service: DBgetById) : Binder() {
        fun setCallback(callback: (FavPic?) -> Unit) {
            service.callback = callback
        }
    }
}