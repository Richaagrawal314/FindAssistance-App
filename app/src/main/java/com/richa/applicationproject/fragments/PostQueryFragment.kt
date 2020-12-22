package com.richa.applicationproject.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.richa.applicationproject.R
import com.richa.applicationproject.databinding.FragmentPostQueryBinding
import com.richa.applicationproject.model.FireStoreJobData
import com.richa.applicationproject.model.ResponderData
import kotlinx.android.synthetic.main.fragment_post_query.*
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class PostQueryFragment : Fragment() {
    private var fStore = FirebaseFirestore.getInstance()
    private lateinit var postQueryBinding: FragmentPostQueryBinding
    private val user = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var title: String
    private lateinit var payment: String
    private var negotiable: Boolean = false
    private lateinit var jobDate: String
    private var urgent: Boolean = false
    private lateinit var description: String
    private lateinit var tags: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //  val view = inflater.inflate(R.layout.fragment_post_query, container, false)
        //used dataBinding
        postQueryBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_post_query, container, false)
        return postQueryBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val buttonPost = postQueryBinding.btnPost

        buttonPost.setOnClickListener { y ->

            val imm: InputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(y.windowToken, 0)

            title = postQueryBinding.editJobTitle.text.toString()
            payment = postQueryBinding.editJobPayment.text.toString()
            if (payment == "0") {

                payment = "Paid Accordingly."

            }

            negotiable = postQueryBinding.switchNegotiable.isChecked
            jobDate = postQueryBinding.editJobDate.text.toString()

            description = postQueryBinding.editJobDescription.text.toString()
            urgent = postQueryBinding.switchUrgent.isChecked
            tags = postQueryBinding.editJobTag.text.toString().toLowerCase(Locale.ROOT)
            val tagList = tags.split(",")
            val tagArray: ArrayList<String> = ArrayList()
            tagArray.addAll(tagList)

            if (validateDetail() && user != null) {
                val docRef2 = fStore.collection("JobQueryData")
                Log.i("TAG", "postQuery: paymnet  $payment")
                val jobData = FireStoreJobData(
                    title, payment, negotiable, jobDate,
                    description, urgent, user,
                    tagArray
                )
                val userRef = fStore.document("Users/$user")
                docRef2.add(jobData).addOnSuccessListener {

                    Toast.makeText(context, "Request Added", Toast.LENGTH_SHORT).show()
                    Log.i("TAG", "OnSuccess:  jobData id ${it.id}")

                    //append to already existing array
                    userRef.update("dorderHistory", FieldValue.arrayUnion(it.id))
                    //for notification purpose
                    val responderRef = userRef.collection("Responder")
                    responderRef.document(it.id).set(ResponderData())

//                    Log.i("TAG", "PostQuery: new sub-Collection In user created")
                }.addOnFailureListener {

                    Log.i("TAG", "OnFailure: ${it.message}")
                    Toast.makeText(context, "Some Error Occurred. Try again.", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }


    private fun validateDetail(): Boolean {
        var isValid = true

        if (title.isBlank()) {
            editJobTitle.error = getString(R.string.error_empty_space)
            isValid = false
        } else {
            editJobTitle.error = null
        }
        if (payment.isBlank()) {
            editJobPayment.error = getString(R.string.error_empty_space)
            isValid = false
        } else {
            editJobPayment.error = null
        }
        if (description.isBlank()) {
            editJobDescription.error = getString(R.string.error_empty_space)
            isValid = false
        } else {
            editJobDescription.error = null
        }

        when {
            (jobDate.isBlank()) -> {
                editJobDate.error = getString(R.string.error_empty_space)
                isValid = false
            }
            (validateDate(jobDate)) -> {
                editJobDate.error = getString(R.string.error_invalid_date)
                isValid = false
            }
            else -> {
                editJobDate.error = null
            }
        }
        if (tags.isBlank()) {
            editJobTag.error = getString(R.string.error_empty_tags)
            isValid = false
        } else {
            editJobTag.error = null
        }

        return isValid
    }

    private fun validateDate(jobDate: String): Boolean {
        return false
    }


}