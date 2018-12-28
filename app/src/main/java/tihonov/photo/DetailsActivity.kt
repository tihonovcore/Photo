package tihonov.photo

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlin.concurrent.thread

class DetailsActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private var urlAddress = ""
    private lateinit var favorite: Switch
    private lateinit var list: MutableList<FavPic>
    private lateinit var instance: Instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        favorite = findViewById(R.id.favorite)
        favorite.setOnCheckedChangeListener(this)

        instance = applicationContext as Instance
        if (intent.hasExtra("image_url") && intent.hasExtra("user_name")) {
            val name = image_description
            name.text = intent.getStringExtra("user_name")
            urlAddress = intent.getStringExtra("image_url")

            val intent = Intent(this, DBgetById::class.java)
            intent.putExtra("image_url", urlAddress)
            bindService(intent, serviceConnection, BIND_AUTO_CREATE)
            startService(intent)

            Picasso.get().load(urlAddress)
                    .tag(MainActivity::class.java)
                     .placeholder(R.color.colorAccent)
                    .error(R.color.colorPrimaryDark)
                    .into(myImage)
        }
    }

    var bind = false
    private var binder: DBgetById.MyBinder? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            bind = true
            binder = service as DBgetById.MyBinder
            binder!!.setCallback { pic ->
                if (pic != null) {
                    favorite.toggle()
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            bind = false
            binder = null
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
                instance.favPicDao.insert(FavPic(urlAddress, intent.getStringExtra("user_name")))
            } else {
                instance.favPicDao.delete(FavPic(urlAddress, intent.getStringExtra("user_name")))
            }
            list = instance.favPicDao.all
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Picasso.get().cancelTag(MainActivity::class.java)
    }

}
