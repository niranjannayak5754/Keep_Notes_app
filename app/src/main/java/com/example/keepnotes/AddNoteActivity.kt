package com.example.keepnotes

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.keepnotes.databinding.ActivityAddNoteBinding
import com.example.keepnotes.models.Notes
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding

    private val imagesArray = ArrayList<String>()
    var countImages = 0
    private lateinit var userId: String

    private lateinit var handler: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("USER").toString()

        handler = DatabaseHelper(this)

        binding.addPicIV.setOnClickListener {
            if (countImages != 10) {
                ImagePicker.with(this)
                    //...
                    .provider(ImageProvider.BOTH) //Or bothCameraGallery
                    .crop()
                    .maxResultSize(620, 620)
                    .createIntentFromDialog {
                        resultLauncher.launch(it)
                    }
            }
            else
                Toast.makeText(this, "Max Limit Reached", Toast.LENGTH_SHORT).show()
        }

        binding.saveBTN.setOnClickListener {
            saveDataToDatabase()
        }
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
        imagesArray.add(uri.toString())
        when(countImages++){
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

    private fun saveDataToDatabase() {
        val title = binding.titleET.text.toString()
        val description = binding.descriptionET.text.toString()

        if (!checkTitle(title)) {
            Toast.makeText(this, "Title minimum characters required: 5", Toast.LENGTH_SHORT).show()
            return
        }
        if (!checkDescription(description)) {
            Toast.makeText(this, "Description minimum characters required: 100", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // calling the addNote function of the database helper class
        if(handler.addNote(userId,title, description, imagesArray)) {
            val intent = Intent(this, MainActivity:: class.java)
            intent.putExtra("USER",userId)
            finishAffinity()
            startActivity(intent)
        }
        else
            Toast.makeText(this, "Process Failed", Toast.LENGTH_SHORT).show()
    }

    private fun checkTitle(title: String): Boolean {
        if (title.length < 5)
            return false
        return true
    }

    private fun checkDescription(description: String): Boolean {
        if (description.length < 100)
            return false
        return true
    }
}