package tihonov.photo

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.gson.Gson
import java.util.ArrayList
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    var bind = false

    data class Urls(var full: String?, var regular: String?)
    data class User(var name: String?)
    data class Photo(var id: String?, var description: String?, var urls: Urls?, var user: User?)

    private var userName = ArrayList<String>()
    private var imageUrl = ArrayList<String>()
    private var unsplashAnswer: String? = null

    private val API = "https://api.unsplash.com/photos/"
    private val SETTINGS = "random/?count=30"
    private val CLIENT_ID = "d3b053ebcb9d702a5612e128b366cfb4f14dbfd72b2d08fd995a990c08611719"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val intent = Intent(this, StringLoader::class.java)
            intent.putExtra(LINK, "$API$SETTINGS&client_id=$CLIENT_ID")
            bindService(intent, serviceConnection, BIND_AUTO_CREATE)
            startService(intent)
        } else {
            imageUrl = savedInstanceState.getStringArrayList(URLS)
            userName = savedInstanceState.getStringArrayList(NAMES)
            startRecyclerView()
        }
    }

    var binder: StringLoader.MyBinder? = null
    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            bind = true
            binder = service as StringLoader.MyBinder
            binder!!.setCallback { p -> parseJson(p) }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            bind = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArrayList(NAMES, userName)
        outState.putStringArrayList(URLS, imageUrl)
        super.onSaveInstanceState(outState)
    }

    private fun parseJson(result: String?) {
        unsplashAnswer = result
        val list = Gson().fromJson<List<Photo>>(unsplashAnswer, object : TypeToken<List<Photo>>() {}.type)
        for (photo in list) {
            imageUrl.add(photo.urls!!.regular!!)
            userName.add(photo.user!!.name!!)
        }

        startRecyclerView()
    }

    private fun startRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = RecyclerViewAdapter(this, userName, imageUrl)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    companion object {
        private val NAMES = MainActivity::class.java.name + ".names"
        private val URLS = MainActivity::class.java.name + ".urls"
        val LINK = MainActivity::class.java.name + ".link"
    }
}
