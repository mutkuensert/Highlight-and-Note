package com.mutkuensert.highlightandnote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.mutkuensert.highlightandnote.databinding.NotesRowBinding
import com.mutkuensert.highlightandnote.model.NoteClass
import com.mutkuensert.highlightandnote.util.FROM_APP_AND_RECYCLERVIEW
import com.mutkuensert.highlightandnote.util.FROM_INTENT_AND_RECYCLERVIEW
import com.mutkuensert.highlightandnote.view.MainFragmentDirections

class NotesRecyclerAdapter(val notes : ArrayList<NoteClass>) : RecyclerView.Adapter<NotesRecyclerAdapter.NotesViewHolder>(){
    var fromIntentOrApp: Int = FROM_APP_AND_RECYCLERVIEW
    class NotesViewHolder(val binding : NotesRowBinding): RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = NotesRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes.get(position).note
        if(note!!.length > 300){
            holder.binding.textView.text = note.subSequence(0,300).toString() + "..."
        }else {
            holder.binding.textView.text = notes.get(position).note
        }
        holder.itemView.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment(notes.get(position).uid, fromIntentOrApp)
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }
    fun appHasBeenExecutedByIntent(isTrue: Boolean){
        if(isTrue){
            fromIntentOrApp = FROM_INTENT_AND_RECYCLERVIEW
        }else{
            fromIntentOrApp = FROM_APP_AND_RECYCLERVIEW
        }
    }

    fun submitList(newListOfNote : List<NoteClass>){
        notes.clear()
        notes.addAll(newListOfNote)
        notifyDataSetChanged()
    }
}