package com.example.obstickleavoider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.net.MalformedURLException

private lateinit var register : TextView
private lateinit var email : EditText
private lateinit var password : EditText
private lateinit var signIn: Button
private lateinit var progressBar : ProgressBar
private lateinit var forgotPassword : TextView

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.register)
        register.setOnClickListener(this)

        signIn = findViewById(R.id.signIn)
        signIn.setOnClickListener(this)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)

        progressBar = findViewById(R.id.progressBar)
        progressBar.bringToFront()

        forgotPassword = findViewById(R.id.forgotPassword)
        forgotPassword.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            (R.id.register) -> {
                val intent = Intent(this, RegisterUser::class.java)
                startActivity(intent)
                finish()
            }
            (R.id.signIn) -> {
                userLogin()
            }
            (R.id.forgotPassword) -> {
                val intent = Intent(this, ForgotPassword::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun userLogin() {
        val emailTxt = email.text.toString().trim()
        val passwordTxt = password.text.toString().trim()

        if (emailTxt.isEmpty()) {
            email.error = "Email is required!"
            email.requestFocus()
            return
        }

        if (!isValidEmail(emailTxt)) {
            email.error = "Please provide valid email"
            email.requestFocus()
            return
        }

        if (passwordTxt.isEmpty()) {
            password.error = "Password is required!"
            password.requestFocus()
            return
        }

        if (!isValidPassword(passwordTxt)) {
            password.error = "Min length should be 6 characters."
            password.requestFocus()
            return
        }
        progressBar.visibility = View.VISIBLE

        mAuth?.signInWithEmailAndPassword(emailTxt, passwordTxt)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //redirect to user profile
                val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
                val intent = Intent(this, ProfileActivity::class.java)

                if(user?.isEmailVerified == true){
                    
                    startActivity(intent)
                } else {
                    user?.sendEmailVerification();
                    Toast.makeText(this, "Check your email to verify your account!", Toast.LENGTH_LONG).show()
                }
                progressBar.visibility = View.GONE
            } else {
                Toast.makeText(this, "Failed to login! Try again!", Toast.LENGTH_LONG).show()
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

    fun isValidPassword(target: CharSequence?): Boolean {
        return !(TextUtils.isEmpty(target) or (target?.length!! < 6))
    }
}
