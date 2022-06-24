package com.example.keepnotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.keepnotes.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private lateinit var handler: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = DatabaseHelper(this)

        nameFocusListener()
        emailFocusListener()
        passwordFocusListener()
        phoneFocusListener()

        binding.submitBTN.setOnClickListener { submitForm() }

    }

//    functions are defined here

    private fun submitForm() {
        binding.nameContainer.helperText = validName()
        binding.emailContainer.helperText = validEmail()
        binding.passwordContainer.helperText = validPassword()
        binding.phoneContainer.helperText = validPhone()

        val validName = binding.nameContainer.helperText == null
        val validEmail = binding.emailContainer.helperText == null
        val validPassword = binding.passwordContainer.helperText == null
        val validPhone = binding.phoneContainer.helperText == null

        if (validName && validEmail && validPassword && validPhone)
            insertData()
        else
            invalidForm()
    }

    private fun insertData() {
        val name: String = binding.nameET.text.toString()
        val email: String = binding.emailET.text.toString().trim().lowercase()
        val password: String = binding.passwordET.text.toString()
        val phone: String = binding.phoneET.text.toString()

        if (handler.checkUser(email)) {
            AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("Email already exists")
                .setPositiveButton("Okay") { _, _ ->
                    //empty
                }.show()
            return
        }
        if (!handler.insertUserData(name, email, password, phone)) {
            Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LogInActivity::class.java)
        finishAffinity()
        startActivity(intent)
    }

    private fun invalidForm() {
        var message = ""
        if (binding.nameContainer.helperText != null)
            message += "Name: " + binding.nameContainer.helperText
        if (binding.emailContainer.helperText != null)
            message += "\nEmail: " + binding.emailContainer.helperText
        if (binding.passwordContainer.helperText != null)
            message += "\nPassword: " + binding.passwordContainer.helperText
        if (binding.phoneContainer.helperText != null)
            message += "\nPhone No: " + binding.phoneContainer.helperText

        AlertDialog.Builder(this)
            .setTitle("Invalid Form")
            .setMessage(message)
            .setPositiveButton("Okay") { _, _ ->
                //empty
            }.show()
    }

    private fun nameFocusListener() {
        binding.nameET.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.nameContainer.helperText = validName()
            }
        }
    }

    private fun validName(): String? {
        val nameText = binding.nameET.text.toString()
        if (nameText.length < 4) {
            return "Name Must Be At Least 4 Character Long"
        }
        if (nameText.matches(".*[0-9].*".toRegex())) {
            return "Must Not Contain Digits"
        }
        if (nameText.matches(".*[@#\$%^&+=!_<>-].*".toRegex())) {
            return "Must Not Contain Any Special Character"
        }
        return null
    }

    private fun emailFocusListener() {
        binding.emailET.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.emailContainer.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.emailET.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return "Invalid Email Address"
        }
        return null
    }

    private fun passwordFocusListener() {
        binding.passwordET.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.passwordContainer.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val passwordText = binding.passwordET.text.toString()
        val nameText = binding.nameET.text.toString()

        if(passwordText == nameText.trim()){
            return "Password should not be your name"
        }

        if (!passwordText.matches("(([a-z])(?=(?:.*?[A-Z]){2})(?=.*?[!@#$?%&_])(?=(?:.*?[0-9]){2}).*)".toRegex())) {
            return "Must Contain first lowercase, 2 UpperCase, 2 Digits and 1 special character"
        }

        if (passwordText.length < 8) {
            return "Password must be 8 characters long"
        }

        return null
    }

    private fun phoneFocusListener() {
        binding.phoneET.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.phoneContainer.helperText = validPhone()
            }
        }
    }

    private fun validPhone(): String? {
        val phoneText = binding.phoneET.text.toString()
        if (!phoneText.matches("[6789][0-9]{9}".toRegex())) {
            return "Indian Numbers Only starting with 6,7,8 or 9"
        }
        return null
    }
}