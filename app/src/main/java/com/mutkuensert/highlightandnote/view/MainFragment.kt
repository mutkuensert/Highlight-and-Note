package com.mutkuensert.highlightandnote.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mutkuensert.highlightandnote.R
import com.mutkuensert.highlightandnote.adapter.NotesRecyclerAdapter
import com.mutkuensert.highlightandnote.databinding.FragmentMainBinding
import com.mutkuensert.highlightandnote.service.SingletonClass
import com.mutkuensert.highlightandnote.util.FROM_APP_AND_NEW_BUTTON
import com.mutkuensert.highlightandnote.util.FROM_INTENT_AND_SNACKBAR_NEW_BUTTON
import com.mutkuensert.highlightandnote.viewmodel.MainFragmentViewModel


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainFragmentViewModel by viewModels()
    private val recyclerAdapter = NotesRecyclerAdapter(arrayListOf())
    private var snackBar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.createDataAccessObject(requireContext())

        setMenus()

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.finish()
        }
        callback.isEnabled

        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.adapter = recyclerAdapter

        viewModel.getNotes()

        observeLiveData()
        checkIntent()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackBar?.dismiss()
        _binding = null
    }

    private fun askToCreateNewNoteWithTheReceivedText() {
        recyclerAdapter.appHasBeenExecutedByIntent(true)
        snackBar = Snackbar.make(binding.root, R.string.snack_message, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.menu_new) {
                val action = MainFragmentDirections.actionMainFragmentToDetailFragment(
                    0,
                    FROM_INTENT_AND_SNACKBAR_NEW_BUTTON
                ) //Note id is 0 because a new note is going to be created.
                findNavController().navigate(action)
            }
        snackBar!!.show()
    }

    private fun checkIntent() {
        if (requireActivity().intent.action != null &&
            (requireActivity().intent.action.equals(Intent.ACTION_PROCESS_TEXT) || requireActivity().intent.action.equals(
                Intent.ACTION_SEND
            ))
        ) {

            if (requireActivity().intent.hasExtra(Intent.EXTRA_PROCESS_TEXT)) {
                SingletonClass.receivedText =
                    requireActivity().intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)
                askToCreateNewNoteWithTheReceivedText()
            } else if (requireActivity().intent.hasExtra(Intent.EXTRA_TEXT)) {
                SingletonClass.receivedText =
                    requireActivity().intent.getStringExtra(Intent.EXTRA_TEXT)
                askToCreateNewNoteWithTheReceivedText()
            }
        }
    }


    private fun observeLiveData() {
        viewModel.notes.observe(viewLifecycleOwner) {
            recyclerAdapter.submitList(it)
        }
    }


    private fun setMenus() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if (!menu.hasVisibleItems()) {
                    menuInflater.inflate(R.menu.optionsmenu, menu)
                }
                menu.findItem(R.id.newNote).isVisible = true
                menu.findItem(R.id.deleteNote).isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.newNote) {
                    val action = MainFragmentDirections.actionMainFragmentToDetailFragment(
                        0,
                        FROM_APP_AND_NEW_BUTTON
                    ) //noteId is 0 because this is not an existing note.
                    findNavController().navigate(action)
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


}