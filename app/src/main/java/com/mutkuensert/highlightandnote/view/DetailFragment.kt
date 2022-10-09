package com.mutkuensert.highlightandnote.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.addCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.mutkuensert.highlightandnote.R
import com.mutkuensert.highlightandnote.databinding.FragmentDetailBinding
import com.mutkuensert.highlightandnote.model.NoteClass
import com.mutkuensert.highlightandnote.service.SingletonClass
import com.mutkuensert.highlightandnote.viewmodel.DetailFragmentViewModel


class DetailFragment : Fragment() {
    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : DetailFragmentViewModel
    private var kaynak : Int = 0
    private var noteId : Int = 0
    private var textControl : Int = 0
    private lateinit var recyclerdanGelenNot : NoteClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuleriAyarla()
        val singleton = SingletonClass.AlinanText
        geriTusuAyarlari()
        argumanlariAl(singleton)
    }

    fun geriTusuAyarlari(){
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if(kaynak == 0 || kaynak ==2){
                if(textControl==0){
                    if(binding.editText.text.isNotEmpty()){
                        val yeniNot = NoteClass(binding.editText.text.toString())
                        viewModel.yeniKayit(yeniNot)
                        Toast.makeText(context,R.string.note_saved,Toast.LENGTH_SHORT).show()
                        val action = DetailFragmentDirections.actionDetailFragmentToMainFragment()
                        findNavController().navigate(action)
                    }else{
                        val action = DetailFragmentDirections.actionDetailFragmentToMainFragment()
                        findNavController().navigate(action)
                    }
                }else if (textControl == 1){
                    val yeniNot = NoteClass(binding.editText.text.toString())
                    viewModel.yeniKayit(yeniNot)
                    Toast.makeText(context,R.string.note_saved,Toast.LENGTH_SHORT).show()
                    activity?.let {
                        it.finish()
                    }
                }
            }else if(kaynak==1){//recyclerviewdan gelinirse güncellenecek
                if(textControl==0){
                    recyclerdanGelenNot = NoteClass(binding.editText.text.toString())
                    recyclerdanGelenNot.uid=noteId
                    viewModel.kayitGuncelle(recyclerdanGelenNot)
                    Toast.makeText(context,R.string.note_saved,Toast.LENGTH_SHORT).show()
                    val action = DetailFragmentDirections.actionDetailFragmentToMainFragment()
                    findNavController().navigate(action)
                }else if(textControl==1){
                    val yeniNot = NoteClass(binding.editText.text.toString())
                    yeniNot.uid=noteId
                    viewModel.kayitGuncelle(yeniNot)
                    Toast.makeText(context,R.string.note_saved,Toast.LENGTH_SHORT).show()
                    activity?.let {
                        it.finish()
                    }
                }

            }

        }
        callback.isEnabled
    }

    fun argumanlariAl(singleton: SingletonClass.AlinanText){
        arguments?.let {
            noteId = DetailFragmentArgs.fromBundle(it).noteId
            textControl = DetailFragmentArgs.fromBundle(it).textAlinacak
            kaynak = DetailFragmentArgs.fromBundle(it).kaynakKontrolu

            if(kaynak == 0 && textControl == 1){//menüden yeni kayıt
                binding.editText.setText(singleton.alinanText)
                //textcontrol 0 ise bir şey yapmaya gerek yok
            }else if (kaynak==1){//recyclerviewdan gelindiyse
                recyclerdanGelenNot = NoteClass(binding.editText.text.toString())
                viewModel.kayitGetir(noteId)
                if(textControl==0) {
                    viewModel.note.observe(viewLifecycleOwner, Observer {
                        binding.editText.setText(it.note)
                    })
                }else if(textControl==1){
                    viewModel.note.observe(viewLifecycleOwner, Observer {
                        val notveText = it.note + "\n" + "\n${singleton.alinanText}"
                        binding.editText.setText(notveText)
                    })
                }
            }else if (kaynak == 2){
                binding.editText.setText(singleton.alinanText)
            }

        }
    }

    private fun menuleriAyarla(){
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if(!menu.hasVisibleItems()){menuInflater.inflate(R.menu.optionsmenu,menu)}
                menu.findItem(R.id.silMenu).setVisible(true)
                menu.findItem(R.id.yeniKayit).setVisible(false)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.silMenu) {
                    if (kaynak == 1) { //recyclerview'den gelindiyse kaydı sil. Yeni bir girdi ise zaten silinecek kayıt yok.
                        viewModel.kayitSil(viewModel.note.value!!)
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