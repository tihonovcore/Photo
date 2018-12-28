package tihonov.photo

import android.app.IntentService
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.Looper
import android.os.IBinder

class DBReader : IntentService("Reader") {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var callback: (List<String>, List<String>) -> Unit
    private lateinit var instance: Instance

    override fun onHandleIntent(intent: Intent) {
        instance = applicationContext as Instance

        val userName = ArrayList<String>()
        val imageUrl = ArrayList<String>()

        val list = run {
            instance.favPicDao.all
        }

        for (curr in list) {
            userName.add(curr.userName!!)
            imageUrl.add(curr.url)
        }

        handler.post { callback(userName, imageUrl) }
    }

    override fun onBind(intent: Intent): IBinder? {
        return MyBinder(this)
    }

    class MyBinder(private val service: DBReader) : Binder() {
        fun setCallback(callback: (List<String>, List<String>) -> Unit) {
            service.callback = callback
        }
    }
}
