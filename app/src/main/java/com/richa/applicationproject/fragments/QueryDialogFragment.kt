package com.richa.applicationproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.richa.applicationproject.R
import com.richa.applicationproject.model.FireStoreJobData
import com.richa.applicationproject.model.MyResponsesData

/**
 * this fragment shows the details about a query/Job
 * in the database.
 */
class QueryDialogFragment(private val model: FireStoreJobData) : DialogFragment() {

    private lateinit var closeBtn: ImageButton
    private lateinit var seekBtn: Button
    private lateinit var mTitle: TextView
    private lateinit var mDate: TextView
    private lateinit var mPayment: TextView
    private lateinit var mNegotiable: TextView
    private lateinit var mUrgent: TextView
    private lateinit var mDescription: TextView
    private var mAuth = FirebaseAuth.getInstance()
    private var fStore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.dialogue_fragment_query, container, false)

        closeBtn = view.findViewById(R.id.closeBtn)
        seekBtn = view.findViewById(R.id.btnApply)
        mTitle = view.findViewById(R.id.qTitle)
        mDate = view.findViewById(R.id.qDate)
        mPayment = view.findViewById(R.id.qPayment)
        mDescription = view.findViewById(R.id.qDescription)
        mNegotiable = view.findViewById(R.id.Negotiable)
        mUrgent = view.findViewById(R.id.txtUrgent)

        val myId = mAuth.currentUser!!.uid
        val docRef2 = fStore.document("Users/${model.dUserId}/Responder/${model.documentId}")
        val myResponseCollRef = fStore.collection("Users")
            .document(myId).collection("MyResponses")


        closeBtn.setOnClickListener {
            Log.i("TAG", "DialogFragment: closeBtn Clicked")
            dialog?.dismiss()
        }
        seekBtn.setOnClickListener {
            Log.i("TAG", "DialogFragment: seekBtn Clicked")
            //your response sent to user and saved in his account
            if (model.dUserId != myId) {
                docRef2.update("dresponder", FieldValue.arrayUnion(myId))
                Toast.makeText(
                    context,
                    "Your details will be send to the owner of this query",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context, "You cannot respond to your own request", Toast.LENGTH_SHORT
                ).show()
            }

            //your response saved in your account
            val x = Timestamp.now()
            Log.i("TAG", "DialogueFragment: Timestamp $x")
            Log.i("TAG", "DialogueFragment: docId ${model.documentId}")
            myResponseCollRef.document(model.documentId).set(
                MyResponsesData(
                    model.documentId, model.dUserId, Timestamp.now()
                )
            ).addOnSuccessListener {
                Log.i("TAG", "DialogueFragment: response added to you account.")
            }

        }
        mTitle.text = model.dJobTitle
        mDate.text = model.dJobDate
        mPayment.text = model.dJobPayment
        mDescription.text = model.dJobDescription
        if (model.dNegotiable) {
            mNegotiable.visibility = View.VISIBLE
        }
        if (model.dUrgent) {
            mUrgent.visibility = View.VISIBLE
        }

        return view
    }

}