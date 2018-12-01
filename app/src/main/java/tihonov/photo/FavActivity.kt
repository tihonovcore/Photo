package tihonov.photo

import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import kotlin.concurrent.thread

class FavActivity : AppCompatActivity() {

    var userName = ArrayList<String>()
    var imageUrl = ArrayList<String>()

    val db = Room.databaseBuilder(this, AppDatabase::class.java, "database")
            .build()
    val favPicDao: FavPicDao = db.favpicDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav)

        thread {
            getList()
        }.join()
        startRecyclerView(userName, imageUrl)
    }

    private fun getList() {
        userName = ArrayList()
        imageUrl = ArrayList()

        val list = favPicDao.all
        for (curr in list) {
            userName.add(curr.userName!!)
            imageUrl.add(curr.url)
        }
    }

    private fun startRecyclerView(userName: ArrayList<String>, imageUrl: ArrayList<String>) {
        val recyclerView = findViewById<RecyclerView>(R.id.favRecyclerView)
        val adapter = RecyclerViewAdapter(applicationContext, userName, imageUrl)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onResume() {
        super.onResume()

        thread {
            getList()
        }.join()
        startRecyclerView(userName, imageUrl)
    }
}
