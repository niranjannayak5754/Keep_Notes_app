package com.example.keepnotes.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.keepnotes.R
import com.example.keepnotes.models.Notes

class NotesRVadapter(private val listener: NotesItemClicked) :
    RecyclerView.Adapter<NotesViewHolder>() {

    private val items = ArrayList<Notes>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_layout, parent, false)
        val viewHolder = NotesViewHolder(view)
        view.setOnClickListener { listener.onItemClicked(items[itemCount - 1 - viewHolder.adapterPosition]) }
        return viewHolder
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentItem = items[itemCount -1 - position]
        currentItem.title.also { holder.noteTitle.text = it }
        if(currentItem.imageArray?.size != 0) {
            val imageTitle = currentItem.imageArray?.get(0);
            Glide.with(holder.itemView.context).load(imageTitle).into(holder.noteImage)
        }
        currentItem.description.also { holder.noteDescription.text = it }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateNotes(updatedNotes: ArrayList<Notes>) {
        items.clear()
        items.addAll(updatedNotes)
//        to notify that the data need to be updated to reRun getItemCount, onCreate and onBindView functions
        notifyDataSetChanged()
    }
}

class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val noteTitle: TextView = itemView.findViewById(R.id.title)
    val noteImage: ImageView = itemView.findViewById(R.id.picture)
    val noteDescription: TextView = itemView.findViewById(R.id.description)
}
interface NotesItemClicked {
    fun onItemClicked(item: Notes)
}