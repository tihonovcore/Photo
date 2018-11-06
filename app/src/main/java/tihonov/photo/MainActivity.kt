package tihonov.photo

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.gson.Gson
import java.lang.ref.WeakReference
import java.net.URL
import java.util.ArrayList
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    data class Urls(var full: String?, var regular: String?)
    data class User(var name: String?)
    data class Photo(var id: String?, var description: String?, var urls: Urls?, var user: User?)

    private var userName = ArrayList<String>()
    private var imageUrl = ArrayList<String>()
    private lateinit var downloadTask: MainActivity.DownloadPostsAsyncTask
    var unsplashAnswer: String? = null

    private val API = "https://api.unsplash.com/photos/"
    private val SETTINGS = "random/?count=30"
    private val CLIENT_ID = "d3b053ebcb9d702a5612e128b366cfb4f14dbfd72b2d08fd995a990c08611719"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            downloadTask = MainActivity.DownloadPostsAsyncTask(WeakReference(this))
            downloadTask.execute(URL("$API$SETTINGS&client_id=$CLIENT_ID"))
        } else {
            imageUrl = savedInstanceState.getStringArrayList(URLS)
            userName = savedInstanceState.getStringArrayList(NAMES)
            startRecyclerView()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArrayList(NAMES, userName)
        outState.putStringArrayList(URLS, imageUrl)
        super.onSaveInstanceState(outState)
    }

    private class DownloadPostsAsyncTask(val activity: WeakReference<MainActivity>) : AsyncTask<URL, Int, String>() {
        override fun onPostExecute(res: String?) {
            activity.get()?.let {
                it.unsplashAnswer = res
                if (res != null) {
                    it.parseJson()
                }
            }
        }

        override fun doInBackground(vararg params: URL): String {
            return params[0].openConnection().run {
                connect()
                getInputStream().bufferedReader().readLines().joinToString("")
            }
        }
    }

    private fun parseJson() {
        val list = Gson().fromJson<List<Photo>>(unsplashAnswer, object : TypeToken<List<Photo>>() {}.type)
        for (photo in list) {
            imageUrl.add(photo.urls!!.regular!!)
            userName.add(photo.user!!.name!!)
        }

        startRecyclerView()
    }

    private fun startRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = RecyclerViewAdapter(this, userName, imageUrl)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    companion object {
        private val NAMES = MainActivity::class.java.name + ".names"
        private val URLS = MainActivity::class.java.name + ".urls"
    }
}
