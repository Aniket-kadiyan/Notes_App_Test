package com.example.notesapptest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.notesapptest.data_models.Folder
import com.example.notesapptest.data_models.FolderDatabase
import com.example.notesapptest.data_models.Note
import com.example.notesapptest.data_models.NoteDatabase
import com.example.notesapptest.databinding.ActivityMainBinding
import com.example.notesapptest.retrofit_test.QuotesAPI
import com.example.notesapptest.retrofit_test.RetrofitHelperObject
import com.example.notesapptest.ui.Edit_Notes.EditNoteActivity
import com.example.notesapptest.ui.Edit_Notes.EditNoteViewModel
import com.example.notesapptest.ui.Folders.AddFolderModalAdapter
import com.example.notesapptest.ui.Folders.FoldersViewModel
import com.example.notesapptest.ui.Notes.NoteAdapter
import com.example.notesapptest.ui.Notes.NotesViewModel
import com.example.notesapptest.ui.View_Notes_in_Folder.ViewNotesinFolderActivity
import com.example.notesapptest.ui.View_Notes_in_Folder.ViewNotesinFolderViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding?=null
    val binding
    get()=_binding

    @Inject
    lateinit var foldersDB : FolderDatabase
    @Inject
    lateinit var notesDB : NoteDatabase
    @Inject
    lateinit var quotesAPI: QuotesAPI
    private val foldersViewModel: FoldersViewModel by viewModels()
    private val notesViewModel: NotesViewModel by viewModels()
    private val editNoteViewModel : EditNoteViewModel by viewModels()
    private val viewNotesinFolderViewModel : ViewNotesinFolderViewModel by viewModels()
    private val deletionViewModel : DeletionViewModel by viewModels()
    var addflag=false
    var deliconflag = MutableLiveData<Int>(0)


    fun updateFolderCount(){
        var flist = foldersDB.folderDAO().getCurrrentFolderList()
        for(folder in flist){
            if(folder.folderId!=1) {
                var nlist = notesDB.noteDAO().getCurrentNotesinFolder(folder.folderId)
                foldersDB.folderDAO().updateNoteCount(nlist.size, folder.folderId)
            }
            else{
                var nlist = notesDB.noteDAO().getCurrentNotesList()
                foldersDB.folderDAO().updateNoteCount(nlist.size , folder.folderId)
            }
        }

        Log.d("current folders list", ":: $flist ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //<editor-fold desc=" view model setup Commented">
        //        notesViewModel=ViewModelProvider(this).get(NotesViewModel::class.java)
//        foldersViewModel=ViewModelProvider(this).get(FoldersViewModel::class.java)
//        editNoteViewModel=ViewModelProvider(this).get(EditNoteViewModel::class.java)
//        viewNotesinFolderViewModel=ViewModelProvider(this).get(ViewNotesinFolderViewModel::class.java)
//        deletionViewModel = ViewModelProvider(this).get(DeletionViewModel::class.java)
//        notesDB = Room.databaseBuilder(applicationContext, NoteDatabase::class.java, "notesDB").allowMainThreadQueries().build()
//        foldersDB=Room.databaseBuilder(applicationContext,FolderDatabase::class.java,"foldersDB").allowMainThreadQueries().build()

//         quotesAPI = RetrofitHelperObject().getInstance().create(QuotesAPI::class.java)
//        GlobalScope.launch {
//            val result = quotesAPI.getQuotes()
//            Log.d("retrofit::: ", "onCreate: ${result.body()!!.count}")
//            if (result != null) {
//                var reslist = result.body()!!.results
//                for(i in reslist)
//                Log.d("retrofit::: ", "${i.author}\n\t${i.content}\n------\n")
//            }
//        }
        //</editor-fold>



        GlobalScope.launch {
            notesDB.apply {
                noteDAO().apply {
                    runOnUiThread {
                        notesDB.noteDAO().getNotesList().observeRepeat(this@MainActivity){
                            if(it!=null)
//                                if(it.isNotEmpty()){
                                    Log.d("NOTES LIST DATA::::", "${it}")
                                    notesViewModel.updateNoteList(it)

//                                }
                        }
                    }
                }
            }
            foldersDB.apply {
                folderDAO().apply {
                    if(!this.ifFolderExists("All notes")){
                        var f = Folder(1,"All notes" , 0 , "#ffeb66")
                        addFolder(f)
                    }
                    runOnUiThread {
                        foldersDB.folderDAO().getFolderList().observeRepeat(this@MainActivity){
                            if(it!=null)
                                if(it.isNotEmpty()){
                                    Log.d("FOLDERS LIST DATA::::", "${it}")
                                    foldersViewModel.updateFolderList(it)
                                }
                        }
                    }
                }
            }

            updateFolderCount()

        }
        notesViewModel.updateDB(notesDB)
        foldersViewModel.updateDB(foldersDB)
        setUpToolbar()
        binding?.apply {

            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.foldersFragment , R.id.notesFragment
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)

            mainActivitytopDelete.setOnClickListener {
                if(navController.currentDestination?.id== R.id.notesFragment){
                    deletionViewModel!!.getnotedeleteList().observeOnce(this@MainActivity){
                        for(note in it){
                            GlobalScope.launch { notesDB.noteDAO().deleteNote(note) }
                            runOnUiThread {
                                notesDB.noteDAO().getNotesList().observeRepeat(this@MainActivity){
                                    if(it!=null)
//                                if(it.isNotEmpty()){
                                        Log.d("NOTES LIST DATA::::", "${it}")
                                    notesViewModel.updateNoteList(it)

//                                }
                                }
                            }
                        }
                    }
                    mainActivitytopDelete.visibility=ImageButton.INVISIBLE
                    mainActivitytopDelete.isVisible=false
                }
                if(navController.currentDestination?.id==R.id.foldersFragment){
                    deletionViewModel!!.getfolderdeleteList().observeOnce(this@MainActivity){
                        for(folder in it){
                            GlobalScope.launch {
//                                if(folder.folderId!=1) {
                                    var nlist = notesDB.noteDAO().getCurrentNotesinFolder(folder.folderId)
                                    for (note in nlist) {
                                        notesDB.noteDAO().deleteNote(note)
                                    }
//                                }
//                                else{
//                                    var nlist = notesDB.noteDAO().getCurrentNotesList()
//                                    for (note in nlist) {
//                                        notesDB.noteDAO().deleteNote(note)
//                                    }
//                                }
                                foldersDB.folderDAO().deleteFolder(folder)

                            }
                            runOnUiThread {
                                foldersDB.folderDAO().getFolderList().observeRepeat(this@MainActivity){
                                    if(it!=null){
                                        foldersViewModel.updateFolderList(it)
                                    }
                                }
                            }
                        }
                    }
                    mainActivitytopDelete.visibility=ImageButton.INVISIBLE
                    mainActivitytopDelete.isVisible=false
                }

                mainActivitytopDelete.visibility=ImageButton.INVISIBLE
                mainActivitytopDelete.isVisible=false
                updateFolderCount()
            }

            deletionViewModel!!.getnotedeleteList().observe(this@MainActivity){
                if(it !=null)
                    if(it.isNotEmpty()) {
                        mainActivitytopDelete.visibility=ImageButton.VISIBLE
                        mainActivitytopDelete.isVisible=true
                    }
                else{
                        mainActivitytopDelete.visibility=ImageButton.INVISIBLE
                        mainActivitytopDelete.isVisible=false
                    }
            }
            deletionViewModel!!.getfolderdeleteList().observe(this@MainActivity){
                if(it !=null)
                    if(it.isNotEmpty()) {
                        mainActivitytopDelete.visibility=ImageButton.VISIBLE
                        mainActivitytopDelete.isVisible=true
                    }
                    else{
                        mainActivitytopDelete.visibility=ImageButton.INVISIBLE
                        mainActivitytopDelete.isVisible=false
                    }
            }

            navController.addOnDestinationChangedListener{ controller, destination, arguments ->
               binding?.mainActivitytopDelete!!.visibility=ImageButton.INVISIBLE
                binding?.mainActivitytopDelete!!.isVisible = false
            }

            addButton.setOnClickListener(View.OnClickListener {
                if(navController.currentDestination?.id  == R.id.notesFragment){
                    var n = Note(0,"",1,"")
                    GlobalScope.launch{
                        notesDB.noteDAO().addNote(n)
                        foldersDB.folderDAO().incrementNoteCount(1)
                        Log.d("note being created", "  : $n ")
                    }
                    notesDB.noteDAO().getNotesList().observe(this@MainActivity){
                        if (it != null) {
                            if (it.isNotEmpty()) {
                                Log.d("note being created 2", "  $it \n::::: ${it.last()} ")
                                editNoteViewModel!!.setNote(it.last())
                            }
                        }
                    }
//                    Log.d("note being created 2", "  : $n ")
//                    editNoteViewModel!!.setNote(n)
                }
                if(navController.currentDestination?.id == R.id.foldersFragment){

                    val dialog = BottomSheetDialog(this@MainActivity)
                    dialog.setCancelable(true)
                    Log.d("add folder modal:::", "flag 1 ")
                    val modalView = layoutInflater.inflate(R.layout.add_folder_modal ,null)
                    dialog.setContentView(modalView)
                    val colorPalleteRV = modalView.findViewById<RecyclerView>(R.id.add_folder_modalRV)
                    val addFolderButton = modalView.findViewById<Button>(R.id.addFolderModalbutton)
                    val folderNameETV = modalView.findViewById<EditText>(R.id.editTextFolderName)
                    Log.d("add folder modal:::", "flag 2 ")
                    val colorPallete = listOf<String>("#FFCC66", "#ffeb66", "#ffff66", "#FF9900", "#FF00FF", "#FF0000", "#FFBB86FC", "#FF6200EE", "#FF018786", "#FF03DAC5", "#FFC0CB", "#FF69B4", "#FF10F0", "#F33A6A", "#E0115F", "#E37383")
                    val adapter = AddFolderModalAdapter(colorPallete,addFolderButton)
                    Log.d("add folder modal:::", "flag 3::: $colorPallete \n::: $adapter ")
                    colorPalleteRV.adapter=adapter
                    Log.d("add folder modal:::", "flag 4 ")
                    addFolderButton.isEnabled=false
                    folderNameETV.doOnTextChanged { text, start, before, count ->
                        if(text!!.isBlank()||text.isNullOrEmpty()){
                            addFolderButton.isEnabled =false
                        }
                        else{
                            addFolderButton.isEnabled=true
                        }
                    }
                    addFolderButton.setOnClickListener {
                        Log.d("add folder modal:::", "flag 5 ")
                        var ftitle = folderNameETV.text.toString()
                        if(ftitle.isEmpty() || ftitle.isBlank()){
                            Toast.makeText(this@MainActivity,"please enter a folder name" , Toast.LENGTH_SHORT).show()
                        }else{
                            if(adapter.selectedcolor==""){
                                adapter.selectedcolor="#ffeb66"
                            }
                            else {
                                var f = Folder(0, ftitle, 0, adapter.selectedcolor)
                                Log.d("add folder modal:::", "flag 7 ::: $f ")
                                GlobalScope.launch { foldersDB.folderDAO().addFolder(f) }
                                Log.d("add folder modal:::", "flag 7.2 ::: $f ")
                                foldersDB.folderDAO().getFolderList().observeOnce(this@MainActivity) {
                                    if (it != null)
                                        if (it.isNotEmpty()) {
                                            Log.d("add folder modal:::", "flag 8::: ${it.last()} ")
//                                            viewNotesinFolderViewModel!!.setViewFolder(it.last())
                                        }
                                }
                                Log.d("add folder modal:::", "flag 9 ")
                                navController.navigate(R.id.foldersFragment)
                                dialog.dismiss()
                            }
                        }

                    }
                    Log.d("add folder modal:::", "flag 10 ")
                    dialog.show()
                }
                updateFolderCount()
            })

        }


        editNoteViewModel!!.getNote().observe(this){
            if(it!=null) {
                var intent = Intent(applicationContext, EditNoteActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                var bundle = Bundle()
                bundle.putInt("note_id",it.noteId)
                intent.putExtras(bundle)
                this.applicationContext.startActivity(intent,bundle)
            }
        }
        viewNotesinFolderViewModel!!.getViewFolder().observe(this){
            Log.d("1--main  view folder:::", "$it ")
            if(it!=null) {
                Log.d("2--main  view folder:::", "$it ")
                var intent = Intent(applicationContext, ViewNotesinFolderActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                var bundle = Bundle()
                bundle.putInt("folder_id", it.folderId)
                bundle.putString("folder_title",it.folderTitle)
                intent.putExtras(bundle)
                this.applicationContext.startActivity(intent,bundle)
            }

        }


    }

    override fun onResume() {
        super.onResume()
        updateFolderCount()
    }

    override fun onStart() {
        super.onStart()
        addflag=false
        runOnUiThread {
            notesDB.noteDAO().getNotesList().observe(this@MainActivity){
                if(it!=null)
                    if(it.isNotEmpty()){
                        Log.d("NOTES start DATA::::", "${it}")
                        notesViewModel.updateNoteList(it)

                    }
            }
        }
        runOnUiThread {
            foldersDB.folderDAO().getFolderList().observe(this@MainActivity){
                if(it!=null)
                    if(it.isNotEmpty()){
                        Log.d("FOLDERS start DATA::::", "${it}")
                        foldersViewModel.updateFolderList(it)

                    }
            }
        }


    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.delete_item ,menu)
//        return true
//    }



    private fun setUpToolbar() {
        setSupportActionBar(binding?.topapptoolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
//        supportActionBar?.hide()
    }


}

fun<T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            observer.onChanged(t)
            removeObserver(this)
        }

    })
}
fun<T> LiveData<T>.observeRepeat(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            observer.onChanged(t)
//            removeObserver(this)
        }

    })
}