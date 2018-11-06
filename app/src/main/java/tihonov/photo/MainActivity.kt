package tihonov.photo

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference
import java.net.URL
import java.util.ArrayList
import com.google.gson.reflect.TypeToken
import java.io.IOException


class MainActivity : AppCompatActivity() {

    //vars
    private val mNames = ArrayList<String>()
    private val mImageUrls = ArrayList<String>()
    var result: String? = null
    private lateinit var downloadTask: MainActivity.DownloadPostsAsyncTask


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: started.")

        download()
        //initImageBitmaps()
    }

    private fun download() {
        downloadTask = MainActivity.DownloadPostsAsyncTask(WeakReference(this))
        downloadTask.execute(URL("https://api.unsplash.com/photos/random/?count=30&client_id=d3b053ebcb9d702a5612e128b366cfb4f14dbfd72b2d08fd995a990c08611719"))

    }

    private fun initImageBitmaps() {
        val gson = Gson()

        Log.d("REST", result)

        val listType = object : TypeToken<List<Photo>>() {}.type
        if (result == null) {
            result = "[{\"id\":\"8abHBUrRM60\",\"created_at\":\"2018-09-03T23:35:47-04:00\",\"updated_at\":\"2018-09-12T15:01:36-04:00\",\"width\":3953,\"height\":5921,\"color\":\"#12171B\",\"description\":\"aerial photography of city buildings at daytime\",\"urls\":{\"raw\":\"https://images.unsplash.com/photo-1536032040612-94e02ba0dec1?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjQwNTMxfQ&s=33e367dfdb0544be883753ea27108a8a\",\"full\":\"https://images.unsplash.com/photo-1536032040612-94e02ba0dec1?ixlib=rb-0.3.5&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjQwNTMxfQ&s=705737718940e14429bbb2c08591aacb\",\"regular\":\"https://images.unsplash.com/photo-1536032040612-94e02ba0dec1?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjQwNTMxfQ&s=de11def5e16cb329d8579cb5e094218d\",\"small\":\"https://images.unsplash.com/photo-1536032040612-94e02ba0dec1?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjQwNTMxfQ&s=40aed385141541fbbd0e2e7d7a01a8ff\",\"thumb\":\"https://images.unsplash.com/photo-1536032040612-94e02ba0dec1?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjQwNTMxfQ&s=01c9629b1855ea2b0854b4bd69e97e7f\"},\"links\":{\"self\":\"https://api.unsplash.com/photos/8abHBUrRM60\",\"html\":\"https://unsplash.com/photos/8abHBUrRM60\",\"download\":\"https://unsplash.com/photos/8abHBUrRM60/download\",\"download_location\":\"https://api.unsplash.com/photos/8abHBUrRM60/download\"},\"categories\":[],\"sponsored\":false,\"sponsored_by\":null,\"sponsored_impressions_id\":null,\"likes\":1,\"liked_by_user\":false,\"current_user_collections\":[],\"slug\":null,\"user\":{\"id\":\"cMSgJBBG2Ys\",\"updated_at\":\"2018-10-08T22:39:48-04:00\",\"username\":\"benjagremler\",\"name\":\"Benjam\\u00edn Gremler\",\"first_name\":\"Benjam\\u00edn\",\"last_name\":\"Gremler\",\"twitter_username\":\"benjagremler\",\"portfolio_url\":\"https://www.instagram.com/dronepict/\",\"bio\":\"\\ud83d\\udcf8 Photographer / \\ud83d\\ude81 Droner \\r\\nChilean\\r\\n\",\"location\":\"Santiago, Chile.\",\"links\":{\"self\":\"https://api.unsplash.com/users/benjagremler\",\"html\":\"https://unsplash.com/@benjagremler\",\"photos\":\"https://api.unsplash.com/users/benjagremler/photos\",\"likes\":\"https://api.unsplash.com/users/benjagremler/likes\",\"portfolio\":\"https://api.unsplash.com/users/benjagremler/portfolio\",\"following\":\"https://api.unsplash.com/users/benjagremler/following\",\"followers\":\"https://api.unsplash.com/users/benjagremler/followers\"},\"profile_image\":{\"small\":\"https://images.unsplash.com/profile-fb-1528067183-164601fe344d.jpg?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=db58c6a4067107d83953a7618a330027\",\"medium\":\"https://images.unsplash.com/profile-fb-1528067183-164601fe344d.jpg?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=3d8330887d508afb7ae6e52a3e77d1d8\",\"large\":\"https://images.unsplash.com/profile-fb-1528067183-164601fe344d.jpg?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=1857d381dc0ee69b8566efe802aa853c\"},\"instagram_username\":\"benjagremler\",\"total_collections\":1,\"total_likes\":6,\"total_photos\":40},\"exif\":{\"make\":\"NIKON CORPORATION\",\"model\":\"NIKON D750\",\"exposure_time\":\"1/60\",\"aperture\":\"4.0\",\"focal_length\":\"24.0\",\"iso\":400},\"location\":{\"title\":\"One World Trade Center, New York, United States\",\"name\":\"One World Trade Center\",\"city\":\"New York\",\"country\":\"United States\",\"position\":{\"latitude\":40.7127431,\"longitude\":-74.0133795}},\"views\":4550,\"downloads\":42}]"
        }
        val list = gson.fromJson<List<Photo>>(result, listType)
        for (photo in list) {
            if (photo.urls!!.regular != null) {
                mImageUrls.add(photo.urls!!.regular!!)
            }

            if (photo.user!!.name != null) {
                mNames.add(photo.user!!.name!!)
            } else {
                mNames.add("cat")
            }
        }

        initRecyclerView()
    }

    private class DownloadPostsAsyncTask(val activity: WeakReference<MainActivity>) : AsyncTask<URL, Int, String>() {
        override fun onPostExecute(res: String?) {
            activity.get()?.let {
                it.result = res
                it.initImageBitmaps()
            }
        }

        override fun doInBackground(vararg params: URL): String {
            val response = params[0].openConnection().run {
                connect()
                getInputStream().bufferedReader().readLines().joinToString("")
            }
            return response
        }
    }

    private fun initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.")
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerv_view)
        val adapter = RecyclerViewAdapter(this, mNames, mImageUrls)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    companion object {

        private val TAG = "MainActivity"
    }

    data class Urls(var full: String?, var regular: String?)
    data class User(var name: String?)
    data class Photo(var id: String?, var description: String?, var urls: Urls?, var user: User?)

}

