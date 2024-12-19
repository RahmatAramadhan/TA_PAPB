package com.example.ta_papb

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var btnRegister: Button
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPass: EditText
    private lateinit var btnLogin: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initFirebaseauth()
        setupListeners()

    }

    private fun initFirebaseauth() {
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
    }

    private fun setupListeners() {
        btnLogin = findViewById(R.id.btn_login)
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        etConfirmPass = findViewById(R.id.et_confirmPassword)
        btnRegister = findViewById(R.id.btn_register)

        btnRegister.setOnClickListener { handleRegistration() }
        btnLogin.setOnClickListener { navigateLogin() }
    }

    private fun handleRegistration() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPass.text.toString().trim()

        if (!validateInput(name, email, password, confirmPassword)) {
            return
        }
        registerUser(name, email, password)
    }

    private fun validateInput(name: String, email: String, password: String, confirmPassword: String): Boolean {
        return when {
            name.isEmpty() -> {
                showToast("Name cannot be empty")
                false
            }
            email.isEmpty() -> {
                showToast("Email cannot be empty")
                false
            }
            password.isEmpty() -> {
                showToast("Password cannot be empty")
                false
            }
            confirmPassword.isEmpty() -> {
                showToast("Confirm Password cannot be empty")
                false
            }
            password.length < 6 -> {
                showToast("Password must be at least 6 characters")
                false
            }
            password != confirmPassword -> {
                showToast("Passwords do not match")
                false
            }
            else -> true
        }
    }
    private fun registerUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                    val userData = hashMapOf(
                        "email" to email,
                        "name" to name,
                        "photoPath" to "profile_pictures/default.jpg"
                    )

                    firestore.collection("users").document(userId).set(userData)
                        .addOnSuccessListener{
                            showToast("registration successful! User data saved.")
                            navigateLogin()
                        }
                        .addOnFailureListener{
                            showToast("Failed to save user data: ${it.message}")
                        }
                } else {
                    showToast("Registration failed: ${task.exception?.message}")
                }
            }
    }


    private fun navigateLogin() {
        val intent = Intent(this, Login::class.java).apply {
        }
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}