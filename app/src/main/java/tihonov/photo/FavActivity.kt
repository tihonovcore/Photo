package tihonov.photo

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class FavActivity : AppCompatActivity() {

    private var userName: List<String> = ArrayList()
    private var imageUrl: List<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav)

        val intent = Intent(this, DBReader::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
        startService(intent)
    }

    var bind = false
    private var binder: DBReader.MyBinder? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            bind = true
            binder = service as DBReader.MyBinder
            binder!!.setCallback { names, urls ->
                userName = names
                imageUrl = urls
                startRecyclerView(userName, imageUrl)
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            bind = false
            binder = null
        }
    }

    private fun startRecyclerView(userName: List<String>, imageUrl: List<String>) {
        val recyclerView = findViewById<RecyclerView>(R.id.favRecyclerView)
        val adapter = RecyclerViewAdapter(applicationContext, userName, imageUrl)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onResume() {
        super.onResume()

        val intent = Intent(this, DBReader::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
        startService(intent)
    }
}
