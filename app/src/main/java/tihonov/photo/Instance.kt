package tihonov.photo

import android.app.Application
import android.arch.persistence.room.Room
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient

class Instance : Application() {

    lateinit var client: OkHttpClient
    lateinit var moshi: Moshi
    val db = Room.databaseBuilder(this, AppDatabase::class.java, "database")
            .build()
    val favPicDao: FavPicDao = db.favpicDao()

    @Override
    override fun onCreate() {
        super.onCreate()
        client = OkHttpClient.Builder().build()
        moshi = Moshi.Builder().build()

    }
}
