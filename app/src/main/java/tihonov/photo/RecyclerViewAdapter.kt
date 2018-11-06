package tihonov.photo

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import java.util.ArrayList
//import com.bumptech.glide.Glide

class RecyclerViewAdapter(private val mContext: Context, private val mImageNames: ArrayList<String>, private val mImages: ArrayList<String>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: called.")

        holder.imageName.text = mImageNames[position]

        holder.parentLayout.setOnClickListener {
            Log.d(TAG, "onClick: clicked on: " + mImageNames[position])

            Toast.makeText(mContext, mImageNames[position], Toast.LENGTH_SHORT).show()

            val intent = Intent(mContext, GalleryActivity::class.java)
            intent.putExtra("image_url", mImages[position])
            intent.putExtra("image_name", mImageNames[position])
            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mImageNames.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var imageName: TextView
        internal var parentLayout: RelativeLayout

        init {
            imageName = itemView.findViewById(R.id.image_name)
            parentLayout = itemView.findViewById(R.id.parent_layout)
        }
    }

    companion object {
        private val TAG = "RecyclerViewAdapter"
    }
}
