package tihonov.photo

import android.app.Activity
import android.content.Context
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tihonov.photo.api.Photo
import tihonov.photo.api.UnsplashApi
import android.content.Intent
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Context.INPUT_METHOD_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager


class MainActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var client: OkHttpClient
    private lateinit var moshi: Moshi
    private lateinit var api: UnsplashApi
    private var userCall: Call<List<Photo>>? = null

    private var userName = ArrayList<String>()
    private var imageUrl = ArrayList<String>()

    private lateinit var search: SearchView
    private var query = "beautiful-girls"

    private lateinit var recyclerView: RecyclerView

    private val API = "https://api.unsplash.com/photos/"

    private fun createApi(): UnsplashApi {
        return Retrofit.Builder()
                .baseUrl(API)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(UnsplashApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        client = OkHttpClient.Builder().build()
        moshi = Moshi.Builder().build()

        recyclerView = findViewById(R.id.recyclerView)

        val button: Button = findViewById(R.id.button2)
        button.setOnClickListener {
            val myIntent = Intent(this@MainActivity, FavActivity::class.java)
            this@MainActivity.startActivity(myIntent)
        }

        search = findViewById(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(_query: String): Boolean {
                Toast.makeText(applicationContext, _query,
                        Toast.LENGTH_SHORT).show()
                query = _query
                userName = ArrayList()
                imageUrl = ArrayList()
                recyclerView.removeAllViewsInLayout()
                downloadList()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        if (savedInstanceState == null) {
            downloadList()
            startRecyclerView()
        } else {
            userName = savedInstanceState.getStringArrayList(NAMES)
            imageUrl = savedInstanceState.getStringArrayList(URLS)
            startRecyclerView()
        }
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

    fun downloadList() {
        api = createApi()

        if (userCall != null) {
            userCall!!.cancel()
        }

        val map = mapOf("query" to query, "count" to "30",

                "client_id" to "d3b053ebcb9d702a5612e128b366cfb4f14dbfd72b2d08fd995a990c08611719")

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
//                    if (users == null) onFailure(call, IllegalArgumentException("Users were null"))

                handler.post {
                    for (user in users!!) {
                        userName.add(user.user!!.name!!)
                        imageUrl.add(user.urls!!.regular!!)
                    }
                    startRecyclerView()
                }
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {

            }

        })
    }

    private fun startRecyclerView() {
        val adapter = RecyclerViewAdapter(this, userName, imageUrl)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    companion object {
        private val NAMES = MainActivity::class.java.name + ".names"
        private val URLS = MainActivity::class.java.name + ".urls"
    }
}
