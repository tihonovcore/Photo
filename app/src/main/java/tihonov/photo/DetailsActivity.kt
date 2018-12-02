package tihonov.photo

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlin.concurrent.thread

class DetailsActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private var urlAddress = ""
    private lateinit var favorite: Switch
    private var list: MutableList<FavPic> = ArrayList()

    private val db = Room.databaseBuilder(this, AppDatabase::class.java, "database")
            .build()
    private val favPicDao: FavPicDao = db.favpicDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        favorite = findViewById(R.id.favorite)
        favorite.setOnCheckedChangeListener(this)

        if (intent.hasExtra("image_url") && intent.hasExtra("user_name")) {
            val name = image_description
            name.text = intent.getStringExtra("user_name")
            urlAddress = intent.getStringExtra("image_url")

            var flag = false
            thread {
                val a = favPicDao.getById(urlAddress.hashCode().toLong())
                if (a != null) flag = true
            }.join()

            if (flag) {
                favorite.toggle()
            }

            Picasso.get().load(urlAddress)
                    .tag(MainActivity::class.java)
                    .placeholder(R.color.colorAccent)
                    .error(R.color.colorPrimaryDark)
                    .into(myImage)
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        Toast.makeText(
                this,
                getString(
                        if (isChecked) R.string.add
                        else R.string.remove
                ),
                Toast.LENGTH_SHORT
        ).show()

        thread {
            if (isChecked) {
                favPicDao.insert(FavPic(urlAddress, intent.getStringExtra("user_name")))
            } else {
                favPicDao.delete(FavPic(urlAddress, intent.getStringExtra("user_name")))
            }
            list = favPicDao.all
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Picasso.get().cancelTag(MainActivity::class.java)
    }

}
