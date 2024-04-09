package com.example.banbun_kotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.banbun_kotlin.databinding.ActivitySignUpBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.signupButton.setOnClickListener {
            val signupFullName = binding.signupFullName.text.toString()
            val signupNumber = binding.signupNumber.text.toString()
            val signupUsername = binding.signupUsername.text.toString()
            val signupPassword = binding.signupPassword.text.toString()

            if (signupFullName.isNotEmpty() && signupNumber.isNotEmpty() && signupUsername.isNotEmpty() && signupPassword.isNotEmpty()){
                signupUser(signupFullName, signupNumber, signupUsername, signupPassword)
            } else {
                Toast.makeText(this@SignUp, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginRedirect.setOnClickListener {
            startActivity(Intent(this@SignUp, Login::class.java))
            finish()
        }


    }

    private fun signupUser(fullName: String, number: String, username: String, password: String) {
        databaseReference.orderByChild("fullName").equalTo(fullName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        val id = databaseReference.push().key
                        val userData = UserData(id, fullName, number, username, password)
                        databaseReference.child(id!!).setValue(userData)
                        Toast.makeText(this@SignUp, "Signup Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignUp, Login::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@SignUp, "User already exist", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@SignUp, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }