package com.richa.applicationproject.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.richa.applicationproject.R

class LoginActivity : AppCompatActivity() {

    private lateinit var mEmail: EditText
    private lateinit var mPassword: EditText
    private lateinit var mLogin: Button
    private lateinit var mForgotpassword: TextView
    private lateinit var mRegister: TextView
    private var mAuth = FirebaseAuth.getInstance()
    var isValid: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)


        if (mAuth.currentUser != null) {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        mEmail = findViewById(R.id.txtEmail)
        mPassword = findViewById(R.id.txtpassword)
        mLogin = findViewById(R.id.btnLogin)
        mForgotpassword = findViewById(R.id.txtForgotPassword)
        mRegister = findViewById(R.id.txtRegister)

        mLogin.setOnClickListener {
            if (mEmail.text.isBlank()) {
                mEmail.error = "Email cannot be empty"
                isValid = false
            } else
                isValid = true
            if (mPassword.text.isBlank()) {
                mPassword.error = "Password cannot be empty"
                isValid = false
            } else
                isValid = true
            if (isValid) {
                mAuth.signInWithEmailAndPassword(mEmail.text.toString(), mPassword.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@LoginActivity, "Logging In", Toast.LENGTH_SHORT)
                                .show()
                            intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                task.exception?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
        mRegister.setOnClickListener {
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left)
            finish()
        }
    }
}