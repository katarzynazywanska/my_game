package com.example.obstickleavoider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.obstickleavoider.Game.GameMain
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

private lateinit var logout: Button
private lateinit var user: FirebaseUser
private lateinit var reference: DatabaseReference
private lateinit var userID: String
private lateinit var startGame: Button

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        logout = findViewById(R.id.signOut)
        logout.setOnClickListener(this)

        user = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users")
        userID = user.uid

        val greetingTxt : TextView = findViewById(R.id.greeting)
        val pointsTxt : TextView = findViewById(R.id.points)
        val emailTxt : TextView = findViewById(R.id.email_address)
        startGame = findViewById(R.id.startGame)
        startGame.setOnClickListener(this)

        startGame.setOnClickListener {
            val intent = Intent(this, GameMain::class.java)
            startActivity(intent)
            finish()
        }

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userProfile: User.UserObject? = dataSnapshot.getValue(User.javaClass)

                if (userProfile != null) {
                    val nick: String? = userProfile.nick
                    val email: String? = userProfile.email
                    val points: Int? = userProfile.points

                    greetingTxt.text = "Welcome, " + nick + "!"
                    emailTxt.text = " Email Address: \n " + email
                    pointsTxt.text = " Your best score: \n " + points.toString()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                show_info("Something wrong happened")
            }
        }
        reference.child(userID).addListenerForSingleValueEvent(postListener)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            (R.id.signOut) -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }
            (R.id.startGame) -> {
                val intent = Intent(this, GameMain::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    fun show_info(info:String){
        Toast.makeText(this,info , Toast.LENGTH_LONG).show()
    }
}