package tihonov.photo

import android.content.Intent
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tihonov.photo.api.Photo
import tihonov.photo.api.UnsplashApi
import android.widget.SearchView
import kotlinx.android.synthetic.main.activity_main.*
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var api: UnsplashApi
    private var userCall: Call<List<Photo>>? = null

    private lateinit var search: SearchView
    private lateinit var recycler: RecyclerView
    private lateinit var button: Button

    private var userName = ArrayList<String>()
    private var imageUrl = ArrayList<String>()

    private var query = "beautiful-girls"
    private val API = "https://api.unsplash.com/photos/"

    private lateinit var instance: Instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        instance = applicationContext as Instance
        recycler = recyclerView
        search = searchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(_query: String): Boolean {
                query = _query
                userName = ArrayList()
                imageUrl = ArrayList()
                recycler.removeAllViewsInLayout()
                downloadList()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        button = button2
        button.setOnClickListener {
            val myIntent = Intent(this@MainActivity, FavActivity::class.java)
            this@MainActivity.startActivity(myIntent)
        }

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        if (savedInstanceState == null) {
            downloadList()
        } else {
            userName = savedInstanceState.getStringArrayList(NAMES)
            imageUrl = savedInstanceState.getStringArrayList(URLS)

            startRecyclerView()
        }
    }

    private fun createApi(): UnsplashApi {
        return Retrofit.Builder()
                .baseUrl(API)
                .client(instance.client)
                .addConverterFactory(MoshiConverterFactory.create(instance.moshi))
                .build()
                .create(UnsplashApi::class.java)
    }

    fun downloadList() {
        api = createApi()

        if (userCall != null) {
            userCall!!.cancel()
        }

        val map = mapOf(
                "query" to query,
                "count" to "30",
                "client_id" to "d3b053ebcb9d702a5612e128b366cfb4f14dbfd72b2d08fd995a990c08611719"
        )

        userCall = api.getPhotoList(map)
        userCall!!.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                if (!response.isSuccessful || response.body() == null) {
                    val errorBody = response.errorBody()
                    val str = errorBody?.string()
                    onFailure(call, Throwable(str))
                    return
                }

                val users = response.body()
                if (users == null) onFailure(call, IllegalArgumentException("Users were null"))

                handler.post {
                    for (user in users!!) {
                        userName.add(user.user.name)
                        imageUrl.add(user.urls.regular)
                    }
                    startRecyclerView()
                }
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message,
                        Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun startRecyclerView() {
        val adapter = RecyclerViewAdapter(this, userName, imageUrl)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
    }

    override fun onRestart() {
        super.onRestart()
        window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
    }

    override fun onResume() {
        super.onResume()
        window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArrayList(NAMES, userName)
        outState.putStringArrayList(URLS, imageUrl)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (userCall != null) userCall!!.cancel()
    }

    companion object {
        private val NAMES = MainActivity::class.java.name + ".names"
        private val URLS = MainActivity::class.java.name + ".urls"
    }
}
