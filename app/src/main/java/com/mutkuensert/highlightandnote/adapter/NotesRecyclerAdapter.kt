package com.mutkuensert.highlightandnote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.mutkuensert.highlightandnote.databinding.NotesRowBinding
import com.mutkuensert.highlightandnote.model.NoteClass
import com.mutkuensert.highlightandnote.view.MainFragmentDirections

class NotesRecyclerAdapter(val notlar : ArrayList<NoteClass>, var textControl : Int) : RecyclerView.Adapter<NotesRecyclerAdapter.NotesViewHolder>(){
    class NotesViewHolder(val binding : NotesRowBinding): RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = NotesRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val not = notlar.get(position).note
        if(not!!.length > 300){
            holder.binding.textView.text = not.subSequence(0,300).toString() + "..."
        }else {
            holder.binding.textView.text = notlar.get(position).note
        }
        holder.itemView.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment(notlar.get(position).uid,textControl,1)
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return notlar.size
    }
    fun textControlFun(onay: Int){
        textControl = onay
    }

    fun recycleraGonder(yeniNotListesi : List<NoteClass>){
        notlar.clear()
        notlar.addAll(yeniNotListesi)
        notifyDataSetChanged()
    }
}