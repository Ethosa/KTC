package com.ethosa.ktc.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ethosa.ktc.R
import com.ethosa.ktc.college.objects.New
import com.ethosa.ktc.databinding.WallBinding

class NewsAdapter(var items: List<New>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: WallBinding = WallBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.wall, parent, false)
        return ViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        if (items[pos].img == null) {
            holder.binding.root.removeView(holder.binding.blurredBackground)
            holder.binding.root.removeView(holder.binding.sourceImage)
        }
        holder.binding.wallTitle.text = items[pos].title
        holder.binding.wallText.text = items[pos].description
        holder.binding.wallDate.text = items[pos].date
    }

    override fun getItemCount() = items.size
}