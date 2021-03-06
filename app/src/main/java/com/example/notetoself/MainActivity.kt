package com.example.notetoself

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    /*// Temporary code private
      var tempNote = Note()*/
   //private val noteList = ArrayList<Note>()
    private var mSerializer: JSONSerializer? = null
    private var noteList: ArrayList<Note>? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: NoteAdapter? = null
    private var showDividers: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            val dialog = DialogNewNote()
            dialog.show(supportFragmentManager, "")
        }
        mSerializer = JSONSerializer("NoteToSelf.json",
                applicationContext)
        try {
            noteList = mSerializer!!.load()
        } catch (e: Exception) {
            noteList = ArrayList()
            Log.e("Error loading notes: ", "", e)
        }
        recyclerView =
                findViewById<View>(R.id.recyclerView) as RecyclerView
        adapter = noteList?.let { NoteAdapter(this, it) }
        val layoutManager =
                LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        // Add a neat dividing line between items in the list
        /*recyclerView!!.addItemDecoration(
                DividerItemDecoration(this,
                        LinearLayoutManager.VERTICAL))*/
        // set the adapter
        recyclerView!!.adapter = adapter


    }
    fun createNewNote(n: Note) {
        // Temporary code
        // tempNote = n
        noteList?.add(n)
        adapter!!.notifyDataSetChanged()
    }
    fun showNote(noteToShow: Int) {
        val dialog = DialogShowNote()
        noteList?.get(noteToShow)?.let { dialog.sendNoteSelected(it) }
        dialog.show(supportFragmentManager, "")
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this,
                    SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences(
                "Note to self",
                Context.MODE_PRIVATE
        )
        showDividers = prefs.getBoolean(
                "dividers", true
        )
        // Add a neat dividing line between list items
        if (showDividers)
            recyclerView!!.addItemDecoration(
                    DividerItemDecoration(
                            this, LinearLayoutManager.VERTICAL))
        else {
        // check there are some dividers
        // or the app will crash
            if (recyclerView!!.itemDecorationCount > 0)
                recyclerView!!.removeItemDecorationAt(0)
        }
        }
    private fun saveNotes() {
        try {
            mSerializer!!.save(this.noteList!!)
        } catch (e: Exception) {
            Log.e("Error Saving Notes", "", e)
        }
    }
    override fun onPause() {
        super.onPause()
        saveNotes()
    }
}


