package com.richa.applicationproject.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.richa.applicationproject.R
import com.richa.applicationproject.model.NotificationData

class ResponderDialogFragment(private val model: NotificationData) : DialogFragment() {

    private lateinit var responderName: TextView
    private lateinit var responderAgeGender: TextView
    private lateinit var responderEmail: TextView
    private lateinit var responderPhone: TextView
    private lateinit var resIntText:TextView
    private lateinit var responderInterest: TextView
    private lateinit var loadDialer: Button
    private lateinit var okayBtn: ImageButton
    private var fStore = FirebaseFirestore.getInstance()
    private var userRef1 = fStore.collection("Users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialogue_fragment_contact_respondee, container, false)

        responderName = view.findViewById(R.id.responderName)
        responderAgeGender = view.findViewById(R.id.responderAgeGender)
        responderEmail = view.findViewById(R.id.responderEmail)
        loadDialer = view.findViewById(R.id.ButtonContactNo)
        responderPhone = view.findViewById(R.id.responderContactNo)
        responderInterest = view.findViewById(R.id.profileResInterests)
        resIntText=view.findViewById(R.id.profileResIntereststv)
        okayBtn = view.findViewById(R.id.btnOkay)

        userRef1.document(model.responderId).get()
            .addOnSuccessListener { nameDoc ->
                responderName.text = nameDoc.get("dname").toString()
                responderEmail.text = nameDoc.getString("demail")
                val phoneNo = nameDoc.getString("dphone")
                responderPhone.text = phoneNo
                val age = nameDoc.getString("dage")
                if (age == "") {
                    responderAgeGender.visibility = View.GONE
                    responderInterest.visibility = View.GONE
                    resIntText.visibility=View.GONE
                } else {
                    responderAgeGender.visibility = View.VISIBLE
                    resIntText.visibility=View.VISIBLE
                    responderInterest.visibility = View.VISIBLE
                    responderAgeGender.text = age.plus(", ")
                        .plus(nameDoc.getString("dgender"))
                    responderInterest.text = nameDoc.getString("dinterests")
                }
                val u = Uri.parse("tel:$phoneNo")
                val i = Intent(Intent.ACTION_DIAL, u)
                loadDialer.setOnClickListener {
                    try {
                        startActivity(i)
                    } catch (s: SecurityException) {
                        Toast.makeText(context, "An error Occurred", Toast.LENGTH_SHORT).show()
                    }

                }
            }

        okayBtn.setOnClickListener {
            dialog?.dismiss()
        }
        return view
    }
}