package com.example.moveeventossaltillo

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.moveeventossaltillo.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
if (auth.currentUser!=null){
    startActivity(Intent(this, GruposInvitadosActivity::class.java))
    finish()
}

        binding.btnLogin.setOnClickListener {
            val emailText = binding.etEmail.text.toString().trim()
            val passwordText = binding.etPassword.text.toString().trim()

            if (emailText.isNotEmpty() && passwordText.isNotEmpty()) {
                auth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, GruposInvitadosActivity::class.java))
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