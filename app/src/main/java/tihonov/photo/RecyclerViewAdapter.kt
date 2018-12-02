package tihonov.photo

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView

class RecyclerViewAdapter(private val context: Context, private val userName: List<String>,
                          private val imageUrl: List<String>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_list, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, ind: Int) {
        holder.imageName.text = userName[ind]
        holder.parentLayout.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("image_url", imageUrl[ind])
            intent.putExtra("user_name", userName[ind])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userName.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var imageName: TextView = itemView.findViewById(R.id.image_name)
        internal var parentLayout: RelativeLayout = itemView.findViewById(R.id.parent_layout)
    }

}
