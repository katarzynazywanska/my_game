package com.example.obstickleavoider

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


private lateinit var banner : TextView
private lateinit var registerUser: Button
private lateinit var nick : EditText
private lateinit var email : EditText
private lateinit var password : EditText
private lateinit var progressBar : ProgressBar

class RegisterUser : AppCompatActivity(), View.OnClickListener {

    private var mAuth: FirebaseAuth? = null
    //private var registerUser: Button? = null
    //private var nick : EditText? = null
    //private var email : EditText? = null
    //private var password : EditText? = null
    //private var progressBar : ProgressBar? = null
    //private val banner : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        mAuth = FirebaseAuth.getInstance();
        registerUser = findViewById(R.id.signIn)
        nick = findViewById(R.id.nick)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        progressBar = findViewById(R.id.progressBar)
        banner = findViewById(R.id.banner)

        registerUser.setOnClickListener(this)
        nick.setOnClickListener(this)
        email.setOnClickListener(this)
        password.setOnClickListener(this)
        progressBar.setOnClickListener(this)
        banner.setOnClickListener(this)
    }

    // actions after click on specific text/button :
    override fun onClick(v: View?) {
        when (v?.id) {
            (R.id.banner) -> { // it takes user from register activity to main activity -> where user can log in
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            (R.id.signIn) -> { // it register user by using function below
                registerUser()
            }
        }
    }

    fun registerUser() {
        val emailTxt: String = email.text.toString().trim()
        val passwordTxt: String = password.text.toString().trim()
        val nickTxt: String = nick.text.toString().trim()

        // We check if user has filled every EditText fields correct - if not -> show error message
        if (nickTxt.isEmpty()) {
            nick.error = "Nick is required!"
            nick.requestFocus()
            return
        }
        if (!isValidEmail(emailTxt)) {
            email.error = "Please provide valid email"
            email.requestFocus()
            Log.d("tutaj", emailTxt)
            return
        }
        if (!isValidPassword(passwordTxt)) {
            password.error = "Please provide valid password! Min length should be 6 characters."
            password.requestFocus()
            return
        }

        progressBar.visibility = View.VISIBLE
        mAuth?.createUserWithEmailAndPassword(emailTxt, passwordTxt)?.addOnCompleteListener(
            OnCompleteListener {task -> if(task.isSuccessful) {
                val user = User.UserObject
                user.email = emailTxt
                user.nick = nickTxt
                user.points = 0
                FirebaseAuth.getInstance().currentUser?.let {
                    FirebaseDatabase.getInstance().getReference("Users")
                        .child(it.uid)
                        .setValue(user).addOnCompleteListener(){
                            if (task.isSuccessful){
                                Toast.makeText(this, "User has ben registered successfully!", Toast.LENGTH_LONG).show()
                                progressBar.visibility=View.GONE
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                // po 4s usuwamy tą activity, bo nie jest więcej potrzebna
                                Thread(){
                                    run{
                                        Thread.sleep(4000)
                                        finish()
                                    }
                                }
                                    .start()
                            } else {
                                Toast.makeText(this, "Failed to register! Try again!", Toast.LENGTH_LONG).show()
                                progressBar.visibility=View.GONE
                            }
                        }
                }
            } else {
                Toast.makeText(this, "Failed to register! Try again!", Toast.LENGTH_LONG).show()
                progressBar.visibility=View.GONE
            }
            })
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