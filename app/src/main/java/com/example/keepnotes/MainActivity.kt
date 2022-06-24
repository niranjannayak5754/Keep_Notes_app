package com.example.keepnotes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.keepnotes.adapters.NotesItemClicked
import com.example.keepnotes.adapters.NotesRVadapter
import com.example.keepnotes.databinding.ActivityMainBinding
import com.example.keepnotes.models.Notes

class MainActivity : AppCompatActivity(), NotesItemClicked {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: NotesRVadapter

    private lateinit var handler: DatabaseHelper
    private lateinit var notesArray: ArrayList<Notes>
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("USER").toString()

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        handler = DatabaseHelper(this)
        mAdapter = NotesRVadapter(this)

        fetchData()

        binding.recyclerView.adapter = mAdapter

        binding.addNoteBTN.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            intent.putExtra("USER",userId)
            startActivity(intent)
        }

        binding.logOutBTN.setOnClickListener {
            AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("Are you Sure (YES/NO)")
                .setNegativeButton("NO") { _, _ ->
                    //empty
                }
                .setPositiveButton("YES") { _, _ ->
                    val intent = Intent(this, LogInActivity::class.java)
                    startActivity(intent)
                    finish()
                }.show()
        }
    }

    private fun fetchData() {
        notesArray = ArrayList()
        notesArray = handler.getAllNotes(userId)
        mAdapter.updateNotes(notesArray)
    }

    override fun onItemClicked(item: Notes) {
        val intent = Intent(this, NotesDetailActivity::class.java)
        intent.putExtra("ClickedNote", item)
        intent.putExtra("USER",userId)
        startActivity(intent)
    }
}