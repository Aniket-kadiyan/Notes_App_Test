package com.example.notesapptest.ui.Folders

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.notesapptest.DeletionViewModel
import com.example.notesapptest.data_models.FolderDatabase
import com.example.notesapptest.databinding.FragmentFoldersBinding
import com.example.notesapptest.ui.Notes.NotesViewModel
import com.example.notesapptest.ui.View_Notes_in_Folder.ViewNotesinFolderViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FoldersFragment : Fragment() {

    private var _binding: FragmentFoldersBinding? = null
    private val binding
    get() = _binding!!

    private val viewModelInstance : FoldersViewModel by activityViewModels()
    private val notesviewModelInstance : NotesViewModel by activityViewModels()
    private val viewNotesinFolderViewModel : ViewNotesinFolderViewModel by activityViewModels()
    private val deletionViewModel : DeletionViewModel by activityViewModels()

    private val deleteViewModelNew: DeletionViewModel by viewModels()

    @Inject
    lateinit var foldersDB : FolderDatabase

    fun refreshView(){
        var lst = foldersDB.folderDAO().getCurrrentFolderList()
        Log.d("folder list:", "folder value is ${lst}")
        if (lst.isNotEmpty() ){
            val adapter =
                FolderAdatper(lst, viewNotesinFolderViewModel!!, deletionViewModel!!, 1)
            binding.folderRV.visibility = View.VISIBLE
            binding.folderRV.isVisible = true
            binding.textDashboard.visibility = View.INVISIBLE
            binding.textDashboard.isVisible = false
            binding.folderRV.adapter = adapter
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//       deletionViewModel = ViewModelProvider(requireActivity()).get(DeletionViewModel::class.java)
//        viewNotesinFolderViewModel = ViewModelProvider(requireActivity()).get(ViewNotesinFolderViewModel::class.java)
//        viewModelInstance = ViewModelProvider(requireActivity()).get(FoldersViewModel::class.java)
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
//        viewModelInstance!!.getDB().observe(viewLifecycleOwner){
//            foldersDB=it
//        }
        viewModelInstance!!.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        refreshView()
        viewModelInstance!!.getFolderList().observe(viewLifecycleOwner){
            if(it!=null) {

                Log.d("folder list:", "folder value is ${it}")
                val adapter =
                    FolderAdatper(it, viewNotesinFolderViewModel!!, deletionViewModel!!, 1)
                binding.folderRV.visibility = View.VISIBLE
                binding.folderRV.isVisible = true
                binding.textDashboard.visibility = View.INVISIBLE
                binding.textDashboard.isVisible = false
                binding.folderRV.adapter = adapter

            }
            else{
                var lst = foldersDB.folderDAO().getCurrrentFolderList()
                Log.d("folder list:", "folder value is ${lst}")
                val adapter =
                    FolderAdatper(lst, viewNotesinFolderViewModel!!, deletionViewModel!!, 1)
                binding.folderRV.visibility = View.VISIBLE
                binding.folderRV.isVisible = true
                binding.textDashboard.visibility = View.INVISIBLE
                binding.textDashboard.isVisible = false
                binding.folderRV.adapter = adapter
            }

        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        refreshView()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}