package com.mutkuensert.highlightandnote.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.addCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mutkuensert.highlightandnote.R
import com.mutkuensert.highlightandnote.databinding.FragmentDetailBinding
import com.mutkuensert.highlightandnote.model.NoteClass
import com.mutkuensert.highlightandnote.service.SingletonClass
import com.mutkuensert.highlightandnote.util.FROM_APP_AND_NEW_BUTTON
import com.mutkuensert.highlightandnote.util.FROM_APP_AND_RECYCLERVIEW
import com.mutkuensert.highlightandnote.util.FROM_INTENT_AND_RECYCLERVIEW
import com.mutkuensert.highlightandnote.util.FROM_INTENT_AND_SNACKBAR_NEW_BUTTON
import com.mutkuensert.highlightandnote.viewmodel.DetailFragmentViewModel


class DetailFragment : Fragment() {
    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailFragmentViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.createDataAccessObject(requireContext())

        setMenu()
        setBackButtonCallback()
        getNoteOrReceivedTextToScreen()
    }

    private fun setBackButtonCallback(){
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {

            if(args.source == FROM_APP_AND_NEW_BUTTON){
                if(binding.editText.text.isNotEmpty()){
                    val newNote = NoteClass(binding.editText.text.toString())
                    viewModel.newNote(newNote)
                    Toast.makeText(context,R.string.note_saved,Toast.LENGTH_SHORT).show()
                }
                val action = DetailFragmentDirections.actionDetailFragmentToMainFragment()
                findNavController().navigate(action)

            }else if(args.source == FROM_APP_AND_RECYCLERVIEW){
                val noteFromRecyclerView = NoteClass(binding.editText.text.toString())
                noteFromRecyclerView.uid = args.noteId
                viewModel.updateNote(noteFromRecyclerView)
                Toast.makeText(context,R.string.note_saved,Toast.LENGTH_SHORT).show()
                val action = DetailFragmentDirections.actionDetailFragmentToMainFragment()
                findNavController().navigate(action)

            }else if(args.source == FROM_INTENT_AND_SNACKBAR_NEW_BUTTON){
                val newNote = NoteClass(binding.editText.text.toString())
                viewModel.newNote(newNote)
                Toast.makeText(context,R.string.note_saved,Toast.LENGTH_SHORT).show()
                activity?.finish()

            }else if(args.source == FROM_INTENT_AND_RECYCLERVIEW){
                val newNote = NoteClass(binding.editText.text.toString())
                newNote.uid = args.noteId
                viewModel.updateNote(newNote)
                Toast.makeText(context,R.string.note_saved,Toast.LENGTH_SHORT).show()
                activity?.finish()
            }
        }
        callback.isEnabled
    }

    private fun getNoteOrReceivedTextToScreen(){

        if(args.source == FROM_INTENT_AND_SNACKBAR_NEW_BUTTON){
            binding.editText.setText(SingletonClass.receivedText)

        }else if (args.source == FROM_APP_AND_RECYCLERVIEW){
            viewModel.getNote(args.noteId)
            viewModel.note.observe(viewLifecycleOwner) {
                binding.editText.setText(it.note)
            }
        }else if(args.source == FROM_INTENT_AND_RECYCLERVIEW){
                viewModel.note.observe(viewLifecycleOwner) {
                    val oldNoteAndNewText = it.note + "\n" + "\n${SingletonClass.receivedText}"
                    binding.editText.setText(oldNoteAndNewText)
                }
            }
        }


    private fun setMenu(){
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if(!menu.hasVisibleItems()){menuInflater.inflate(R.menu.optionsmenu,menu)}
                menu.findItem(R.id.deleteNote).isVisible = true
                menu.findItem(R.id.newNote).isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.deleteNote) {
                    if (args.source == FROM_APP_AND_RECYCLERVIEW || args.source == FROM_INTENT_AND_RECYCLERVIEW) {
                        viewModel.deleteNote(viewModel.note.value!!)
                        Toast.makeText(context,R.string.note_deleted,Toast.LENGTH_SHORT).show()
                    }
                    val action = DetailFragmentDirections.actionDetailFragmentToMainFragment()
                    findNavController().navigate(action)
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}