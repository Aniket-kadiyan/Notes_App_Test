package com.example.notesapptest.ui.Notes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapptest.DeletionViewModel
import com.example.notesapptest.data_models.FolderDatabase
import com.example.notesapptest.data_models.Note
import com.example.notesapptest.data_models.NoteDatabase
import com.example.notesapptest.databinding.FragmentNotesBinding
import com.example.notesapptest.retrofit_test.QuotesAPI
import com.example.notesapptest.ui.Edit_Notes.EditNoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.absoluteValue
@AndroidEntryPoint
class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    val binding
    get() = _binding!!

    private val viewModelInstance : NotesViewModel by activityViewModels()
    private val editNoteViewModel : EditNoteViewModel by activityViewModels()
    private val deletionViewModel : DeletionViewModel by activityViewModels()
    @Inject
    lateinit var notesDB : NoteDatabase
    @Inject
    lateinit var foldersDB : FolderDatabase
    @Inject
    lateinit var qts : QuotesAPI
    var dellist = arrayListOf<Note>(Note(0,"",1,""))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dellist.clear()
//        viewModelInstance = ViewModelProvider(requireActivity()).get(NotesViewModel::class.java)
//        editNoteViewModel = ViewModelProvider(requireActivity()).get(EditNoteViewModel::class.java)
//        deletionViewModel = ViewModelProvider(requireActivity()).get(DeletionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        _binding = FragmentNotesBinding.inflate(layoutInflater)
            dellist.clear()
        viewModelInstance!!.text.observe(viewLifecycleOwner) {
            binding.textHome.text=it
        }
      binding.textHome.setOnClickListener {
        GlobalScope.launch {
            var lst = qts.getQuotes().body()!!.results
            for(i in lst){
                notesDB.noteDAO().addNote(Note(0,i.author,1,i.content))
                foldersDB.folderDAO().incrementNoteCount(1)
            }
        }
      }
        viewModelInstance!!.getNoteList().observe(viewLifecycleOwner){
            if(it!=null)
                if(it.isNotEmpty()){
                    var adapter = NoteAdapter(it,editNoteViewModel!!,deletionViewModel!!,1)
                    binding.notesRV.visibility= View.VISIBLE
                    binding.notesRV.isVisible = true
                    binding.textHome.visibility=View.INVISIBLE
                    binding.textHome.isVisible=false
                    binding.textHome.isClickable=false
                    binding.notesRV.adapter=adapter
                    dellist=adapter.delList
                    Log.d("note adapter", "dellist::: \n$dellist\n\n")
                }
                else {
                    binding.notesRV.visibility= View.INVISIBLE
                    binding.notesRV.isVisible = false
                    binding.textHome.visibility=View.VISIBLE
                    binding.textHome.isVisible=true
                    binding.textHome.isClickable=true
                    Log.d("ERROR:::", "Data is null now")
                }
        }
        editNoteViewModel!!.getNote().observe(viewLifecycleOwner){
            Log.d("edit Note", "onCreateView: $it")

        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}