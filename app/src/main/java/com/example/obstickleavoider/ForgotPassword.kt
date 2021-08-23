package com.example.obstickleavoider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

private lateinit var emailEditText : EditText
private lateinit var resetPasswordButton : Button
private lateinit var progressBar : ProgressBar

class ForgotPassword : AppCompatActivity(){
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        emailEditText = findViewById(R.id.emailResetPassword)
        progressBar = findViewById(R.id.progressBar)
        mAuth = FirebaseAuth.getInstance()

        resetPasswordButton = findViewById(R.id.resetPaswordButton)
        resetPasswordButton.setOnClickListener{
            resetPassword()
        }
    }

    private fun resetPassword() {
        var emailTxt : String = emailEditText.text.toString().trim()

        if (emailTxt.isEmpty()) {
            emailEditText.error = "Email is required!"
            emailEditText.requestFocus()
            return
        }

        if (!isValidEmail(emailTxt)) {
            emailEditText.error = "Please provide valid email"
            emailEditText.requestFocus()
            return
        }

        progressBar.visibility = View.VISIBLE
        mAuth?.sendPasswordResetEmail(emailTxt)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Check your email to reset your password!", Toast.LENGTH_LONG).show()
                progressBar.visibility = View.GONE
            } else {
                Toast.makeText(this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show()
                progressBar.visibility = View.GONE
            }
        }
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }
}