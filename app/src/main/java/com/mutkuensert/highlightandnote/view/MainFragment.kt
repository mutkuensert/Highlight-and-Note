package com.mutkuensert.highlightandnote.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mutkuensert.highlightandnote.R
import com.mutkuensert.highlightandnote.adapter.NotesRecyclerAdapter
import com.mutkuensert.highlightandnote.databinding.FragmentMainBinding
import com.mutkuensert.highlightandnote.service.SingletonClass
import com.mutkuensert.highlightandnote.viewmodel.MainFragmentViewModel


class MainFragment : Fragment() {
    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : MainFragmentViewModel
    private val recyclerAdapter = NotesRecyclerAdapter(arrayListOf(), 0)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.let {
                it.finish()
            }
        }
        callback.isEnabled

        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.adapter = recyclerAdapter
        viewModel.notlariGetir()
        observeLiveData()

        val singleton = SingletonClass.AlinanText
        if(singleton.kontrol == 1){
            recyclerAdapter.textControlFun(1)
            val sbar = Snackbar.make(binding.root, R.string.snack_message,Snackbar.LENGTH_INDEFINITE).setAction(R.string.menu_new,View.OnClickListener {
                val action = MainFragmentDirections.actionMainFragmentToDetailFragment(0,1,2)
                Navigation.findNavController(binding.root).navigate(action)
            })
            sbar.show()
        }

    }

    fun observeLiveData(){
        viewModel.notes.observe(viewLifecycleOwner, Observer {
            recyclerAdapter.recycleraGonder(it)
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val silItem = menu.findItem(R.id.silMenu)
        silItem.setVisible(false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.optionsmenu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.yeniKayit){
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment(0,0,0)
            Navigation.findNavController(binding.root).navigate(action)
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}