package com.example.moveeventossaltillo

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.moveeventossaltillo.databinding.ActivityMainBinding
import com.example.moveeventossaltillo.utils.Constantes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val emailText = binding.etEmail.text.toString().trim()
            val passwordText = binding.etPassword.text.toString().trim()

            if (emailText.isNotEmpty() && passwordText.isNotEmpty()) {
                auth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, HomeActivity::class.java))
//                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        // Navega al siguiente pantalla o cierra esta actividad
                    } else {
//                        Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }








}