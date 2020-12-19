package com.richa.applicationproject.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.richa.applicationproject.R
import com.richa.applicationproject.model.FirebaseUserDataProject
import kotlinx.android.synthetic.main.register_activity.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var phone: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmedPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var txtLogin: TextView
    private var mAuth = FirebaseAuth.getInstance()
    private var fStore = FirebaseFirestore.getInstance()
    private lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)
        name = findViewById(R.id.tiNameE)  //tiName.editText?.text.toString()
        phone = findViewById(R.id.tiPhoneE) //tiPhone.editText?.text.toString()
        email = findViewById(R.id.tiEmailE)//tiEmail.editText?.text.toString()
        password = findViewById(R.id.tiPasswordE)// tiPassword.editText?.text.toString()
        confirmedPassword =
            findViewById(R.id.tiConfirmPasswordE)// tiConfirmPassword.editText?.text.toString()
        btnSignUp = findViewById(R.id.btnSignUp)
        txtLogin = findViewById(R.id.txtLogin)

        btnSignUp.setOnClickListener {
            ProgressBar.visibility = View.VISIBLE
            if (validateDetails()) {

                mAuth.createUserWithEmailAndPassword(
                    email.text.toString(),
                    password.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Signing Up", Toast.LENGTH_SHORT).show()
                            userID = mAuth.currentUser!!.uid
                            val docRef = fStore.collection("Users").document(userID)
                            val userDataProject = FirebaseUserDataProject(
                                name.text.toString(), email.text.toString(),
                                phone.text.toString(), password.text.toString()
                            )
                            docRef.set(userDataProject).addOnSuccessListener {

                                Log.d("TAG", "OnSuccess: User Added")

                            }.addOnFailureListener {
                                Log.d("TAG", "OnFailure: ${it.message}")
                            }
                            intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                task.exception?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            ProgressBar.visibility = View.GONE
                        }
                    }

            }
            ProgressBar.visibility = View.GONE
        }

        txtLogin.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
            finish()
        }
    }


    private fun validateDetails(): Boolean {
        var isValid = true
        if (name.text.isBlank()) {
            tiName.error = getString(R.string.error_empty_name)
            isValid = false
        } else {
            tiName.error = null
        }

        if (phone.text.isBlank()) {
            tiPhone.error = getString(R.string.error_empty_phone)
            isValid = false
        } else {
            tiPhone.error = null
        }

        when {
            email.text.isBlank() -> {
                tiEmail.error = getString(R.string.error_empty_email)
                isValid = false
            }
            !(Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) -> {
                tiEmail.error = getString(R.string.valid_error)
                isValid = false
            }
            else -> {
                tiEmail.error = null
            }
        }

        if (password.text.isBlank()) {
            tiPassword.error = getString(R.string.error_empty_password)
            isValid = false
        } else {
            tiPassword.error = null
        }

        when {
            (password.text.toString() != confirmedPassword.text.toString()) -> {
                tiConfirmPassword.error = getString(R.string.error_not_matching_passwords)
                isValid = false
            }
            confirmedPassword.text.isBlank() -> {
                tiConfirmPassword.error = getString(R.string.error_empty_password)
                isValid = false
            }
            else -> {
                tiConfirmPassword.error = null
            }
        }

//        if (!isStore.isChecked && !isIndividual.isChecked) {
//            isValid = true
//           // cbStore.requestFocus()
//           // cbIndividual.requestFocus()
//           // tvNoteSignUp.visibility = View.VISIBLE
//        } else {
//            tvNoteSignUp.visibility = View.GONE
//        }
        return isValid
    }
}