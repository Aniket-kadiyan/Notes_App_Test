package com.example.notesapptest.ui.Folders

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.notesapptest.data_models.FolderDatabase
import com.example.notesapptest.databinding.FragmentFoldersBinding
import com.example.notesapptest.ui.Notes.NotesViewModel


class FoldersFragment : Fragment() {

    private var _binding: FragmentFoldersBinding? = null
    private val binding
    get() = _binding!!

    private var viewModelInstance : FoldersViewModel?=null
    lateinit var foldersDB : FolderDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelInstance = ViewModelProvider(requireActivity()).get(FoldersViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val dashboardViewModel =
//            ViewModelProvider(this).get(FoldersViewModel::class.java)

        _binding = FragmentFoldersBinding.inflate(layoutInflater)


        val textView: TextView = binding.textDashboard
        viewModelInstance!!.getDB().observe(viewLifecycleOwner){
            foldersDB=it
        }
       viewModelInstance!!.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        viewModelInstance!!.getFolderList().observe(viewLifecycleOwner){
            if(it!=null)
                if(it.isNotEmpty()){
                    val adapter = FolderAdatper(it)
                    binding.folderRV.visibility=View.VISIBLE
                    binding.folderRV.isVisible=true
                    binding.textDashboard.visibility=View.INVISIBLE
                    binding.textDashboard.isVisible=false
                    binding.folderRV.adapter=adapter

                }
                else {
                    Log.d("ERROR:::", "Data is null now")
                }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}