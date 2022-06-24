package com.example.keepnotes

import android.app.Activity
import android.content.ClipDescription
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import com.example.keepnotes.databinding.ActivityAddNoteBinding
import com.example.keepnotes.databinding.ActivityNotesDetailBinding
import com.example.keepnotes.models.Notes
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar

class NotesDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesDetailBinding
    private lateinit var selectedNote: Notes
    private lateinit var userId: String

    private var counter = 0

    private lateinit var handler: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedNote = intent.getParcelableExtra("ClickedNote")!!
        userId = intent.getStringExtra("USER").toString()

        var prevImagesCount = selectedNote.imageArray?.size
        if (prevImagesCount == null) prevImagesCount = 0

        counter = prevImagesCount

        handler = DatabaseHelper(this)

        setDataToView()

        binding.title.setOnClickListener{
            showBottomSheet(1) // 1 as chooser for processing title update in database
        }

        binding.description.setOnClickListener{
            showBottomSheet(2) // 2 as chooser for processing description update in database
        }

        binding.deleteBTN.setOnClickListener {
            AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("\tAre you Sure (YES/NO)")
                .setNegativeButton("NO") { _, _ ->
                    //empty
                }
                .setPositiveButton("YES") { _, _ ->
                    handler.deleteNote(selectedNote.note_id)
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("USER", userId)
                    finishAffinity()
                    startActivity(intent)
                }.show()
        }

        binding.updateImagesIV.setOnClickListener {
            addImages()
        }

        binding.updateBTN.setOnClickListener {
            if (counter != prevImagesCount) {
                handler.updateImagesInNote(
                    selectedNote.imageArray,
                    prevImagesCount!!,
                    selectedNote.note_id
                )
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("USER", userId)
                startActivity(intent)
                finish()
            }
            else
                Snackbar.make(it,"Add Photo to Update",Snackbar.LENGTH_SHORT).show()
        }

    }

    private fun showBottomSheet(chooser: Int) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.bottom_sheet, findViewById(R.id.bottomSheetLL))

        var updateTV = bottomSheetView.findViewById<TextView>(R.id.updateTV)
        val updateData = bottomSheetView.findViewById<Button>(R.id.updateBtn2)
        if(chooser == 1)
            updateTV.text = binding.title.text
        else
            updateTV.text = binding.description.text

        updateData.setOnClickListener {
            if(chooser == 1) {
                val data = updateTV.text.toString()
                if (updateTitle(data)) runMainActivity()
            }else {
                val data = updateTV.text.toString()
                if (updateDescription(data)) runMainActivity()
            }
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun runMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("USER",userId)
        finishAffinity()
        startActivity(intent)
    }

    private fun updateDescription(description: String): Boolean {
        if(description.length<100) {
            Toast.makeText(this, "Invalid description", Toast.LENGTH_SHORT).show()
            return false
        }
        handler.updateNote(description,2,selectedNote.note_id)
        return true
    }

    private fun updateTitle(title: String):Boolean {
        if(title.length <5){
            Toast.makeText(this, "Invalid Title", Toast.LENGTH_SHORT).show()
            return false
        }
        handler.updateNote(title,1,selectedNote.note_id)
        return true
    }

    private fun addImages() {
        if (counter != 10) {
            ImagePicker.with(this)
                //...
                .provider(ImageProvider.BOTH) //Or bothCameraGallery
                .crop()
                .maxResultSize(620, 620)
                .createIntentFromDialog {
                    resultLauncher.launch(it)
                }
        } else
            Toast.makeText(this, "Max Limit Reached", Toast.LENGTH_SHORT).show()
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                // Use the uri to load the image
                setImage(uri)
            }
        }

    private fun setImage(uri: Uri) {
        selectedNote.imageArray?.add(uri.toString())
        when (counter++) {
            0 -> binding.imageView1.setImageURI(uri)
            1 -> binding.imageView2.setImageURI(uri)
            2 -> binding.imageView3.setImageURI(uri)
            3 -> binding.imageView4.setImageURI(uri)
            4 -> binding.imageView5.setImageURI(uri)
            5 -> binding.imageView6.setImageURI(uri)
            6 -> binding.imageView7.setImageURI(uri)
            7 -> binding.imageView8.setImageURI(uri)
            8 -> binding.imageView9.setImageURI(uri)
            9 -> binding.imageView10.setImageURI(uri)
        }
    }

    private fun setDataToView() {

        binding.title.text = selectedNote.title
        binding.description.text = selectedNote.description

        for ((index, image: String) in selectedNote.imageArray!!.withIndex()) {
            when (index) {
                0 -> binding.imageView1.setImageURI(image.toUri())
                1 -> binding.imageView2.setImageURI(image.toUri())
                2 -> binding.imageView3.setImageURI(image.toUri())
                3 -> binding.imageView4.setImageURI(image.toUri())
                4 -> binding.imageView5.setImageURI(image.toUri())
                5 -> binding.imageView6.setImageURI(image.toUri())
                6 -> binding.imageView7.setImageURI(image.toUri())
                7 -> binding.imageView8.setImageURI(image.toUri())
                8 -> binding.imageView9.setImageURI(image.toUri())
                9 -> binding.imageView10.setImageURI(image.toUri())
            }
        }
    }
}