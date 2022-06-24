package com.example.keepnotes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.keepnotes.databinding.ActivityLogInBinding

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var handler: DatabaseHelper

    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = DatabaseHelper(this)

        binding.signUpTV.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.logInBTN.setOnClickListener {
            logInToApp()
        }
    }


    private fun logInToApp() {
        email = binding.emailET.text.toString()
        val password = binding.passwordET.text.toString()
        if (email.isEmpty()) {
            binding.emailET.requestFocus()
            return
        }
        if (password.isEmpty()) {
            binding.passwordET.requestFocus()
            return
        }
        val valid : Boolean = handler.checkEmailPassword(email, password)
        if (!valid) {
            AlertDialog.Builder(this)
                .setTitle("Invalid")
                .setMessage("\nUsername and Password does not match")
                .setPositiveButton("Okay") { _, _ ->
                    //empty
                }.show()
            return
        }
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("USER",email)
        startActivity(intent)
        finish()
    }
}