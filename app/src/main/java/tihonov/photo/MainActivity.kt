package tihonov.photo

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
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent



class MainActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var client: OkHttpClient
    private lateinit var moshi: Moshi
    private lateinit var api: UnsplashApi
    private var userCall: Call<List<Photo>>? = null

    private var userName = ArrayList<String>()
    private var imageUrl = ArrayList<String>()

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

        val button: Button = findViewById(R.id.button2)
        button.setOnClickListener {
            val myIntent = Intent(this@MainActivity, FavActivity::class.java)
//            myIntent.putExtra("key", list) //Optional parameters
            this@MainActivity.startActivity(myIntent)
        }

        if (savedInstanceState == null) {
            api = createApi()

            if (userCall != null) {
                userCall!!.cancel()
            }

            userCall = api.getPhotoList()
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
        } else {
            userName = savedInstanceState.getStringArrayList(NAMES)
            imageUrl = savedInstanceState.getStringArrayList(URLS)
            startRecyclerView()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArrayList(NAMES, userName)
        outState.putStringArrayList(URLS, imageUrl)
        super.onSaveInstanceState(outState)
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
    }
}
