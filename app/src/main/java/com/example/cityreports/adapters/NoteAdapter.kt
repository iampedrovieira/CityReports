package com.example.cityreports.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.cityreports.R
import com.example.cityreports.entities.Note

class NoteAdapter internal constructor(
        context:Context, val listener:OnItemClickListener
        ):RecyclerView.Adapter<NoteAdapter.LineViewHolder>(){
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notes = emptyList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {

        val itemView = inflater.inflate(R.layout.note_line,parent,false)
        return LineViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return notes.size
    }
    override fun onBindViewHolder(holder: LineViewHolder, position: Int) {

        val currentPlace = notes[position]
        if(currentPlace.description.length>30){
            holder.description.text = currentPlace.description.take(30) +" ..."
        }else{
            holder.description.text = currentPlace.description
        }

    }
    inner class LineViewHolder(itemView: View):RecyclerView.ViewHolder(itemView),View.OnClickListener
        {

        val description: TextView = itemView.findViewById(R.id.noteDescSample)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position:Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(notes[position])
            }
        }
    }
    internal fun setNotes(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

}

interface OnItemClickListener{
    fun onItemClick(note:Note)
}