package com.example.cityreports.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.cityreports.R
import com.example.cityreports.dataclasses.Note

class NoteAdapter(val list:ArrayList<Note>, val listener:OnItemClickListener):RecyclerView.Adapter<NoteAdapter.LineViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {
        val itemView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.note_line,parent,false);
        return LineViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: LineViewHolder, position: Int) {
        val currentPlace = list[position]
        holder.description.text = currentPlace.descripton
    }
    inner class LineViewHolder(itemView: View):RecyclerView.ViewHolder(itemView),View.OnClickListener{
        val description: TextView = itemView.findViewById<TextView>(R.id.noteDescSample)
        init {
            itemView.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            val position:Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }
}

interface OnItemClickListener{
    fun onItemClick(position:Int)
}