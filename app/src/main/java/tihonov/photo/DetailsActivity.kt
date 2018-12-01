package tihonov.photo

import android.arch.persistence.room.Room
import kotlinx.coroutines.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import java.io.File
import kotlin.concurrent.thread

class DetailsActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    var select = false
    var urlStr = ""
    lateinit var file: File
    lateinit var favorite: Switch
    var list: MutableList<FavPic> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        favorite = findViewById(R.id.favorite)

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            Toast.makeText(applicationContext, list.size.toString(),
                    Toast.LENGTH_SHORT).show()
        }

        favorite.setOnCheckedChangeListener(this)

        if (intent.hasExtra("image_url") && intent.hasExtra("user_name")) {
            val name = findViewById<TextView>(R.id.image_description)
            name.text = intent.getStringExtra("user_name")
            urlStr = intent.getStringExtra("image_url")


            var flag = false
            thread {
                val a = favpicdao.getById(urlStr.hashCode().toLong())
                if (a != null) flag = true
            }.join()

            if (flag) {
                favorite.toggle()
            }

            Picasso.get().load(urlStr)
                    .tag(MainActivity::class.java)
                    .placeholder(R.color.colorAccent)
                    .error(R.color.colorPrimaryDark)
                    .into(myImage)
        }
    }

    val db = Room.databaseBuilder(this, AppDatabase::class.java, "database")
            .build()
    val favpicdao: FavPicDao = db.favpicDao()

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
//        Toast.makeText(this, "Отслеживание переключения: " + if (isChecked) "on" else "off",
//                Toast.LENGTH_SHORT).show()

        GlobalScope.launch {
            if (isChecked) {
                favpicdao.insert(FavPic(urlStr, intent.getStringExtra("user_name")))
                select = true
            } else {
                favpicdao.delete(FavPic(urlStr, intent.getStringExtra("user_name")))
                select = false
            }
            list = favpicdao.all
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        intent.putExtra("changed", select)
    }

    override fun onDestroy() {
        //TODO cancel
        super.onDestroy()
    }

    companion object {
        val PHOTO_URL = DetailsActivity::class.java.name + ".photoUrl"
    }

}
