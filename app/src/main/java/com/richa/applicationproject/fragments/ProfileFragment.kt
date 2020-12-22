package com.richa.applicationproject.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.richa.applicationproject.R
import com.richa.applicationproject.activity.FeedbackActivity
import com.richa.applicationproject.activity.LoginActivity
import com.richa.applicationproject.model.FireStoreJobData
import com.richa.applicationproject.model.MyResponsesData
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var orderHistory: TextView
    private lateinit var txtOrderHistory: TextView
    private lateinit var myResponses: TextView
    private lateinit var txtMyResponses: TextView
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profilePhone: TextView
    private lateinit var profileAgeGender: TextView
    private lateinit var profileInterest: TextView
    private lateinit var profileAgeE: EditText
    private lateinit var profileGenderE: EditText
    private lateinit var profileInterestE: EditText
    private lateinit var orderHistoryButton: ImageButton
    private lateinit var myResponseButton: ImageButton
    private lateinit var profileMenu: ImageButton
    private lateinit var doneBtn: Button
    private lateinit var completeProfileCard: RelativeLayout
    private lateinit var rootView: RelativeLayout
    private lateinit var progressBar: ProgressBar
    private var fStore = FirebaseFirestore.getInstance()
    private var user = FirebaseAuth.getInstance().currentUser?.uid
    private var userRef = fStore.document("Users/$user")
    private lateinit var mContext: Context


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)


        orderHistory = view.findViewById(R.id.OrderHistory)
        myResponses = view.findViewById(R.id.MyResponses)
        txtOrderHistory = view.findViewById(R.id.sampleOrderHistory)
        txtMyResponses = view.findViewById(R.id.MyResponseExpandTV)
        profileName = view.findViewById(R.id.profileName)
        profileEmail = view.findViewById(R.id.profileEmail)
        profilePhone = view.findViewById(R.id.profileContactNo)
        profileAgeGender = view.findViewById(R.id.profileAgeGender)
        profileInterest = view.findViewById(R.id.profileInterests)
        profileAgeE = view.findViewById(R.id.EnterAgeTIE)
        profileGenderE = view.findViewById(R.id.EnterGenderTIE)
        profileInterestE = view.findViewById(R.id.EnterInterestE)
        doneBtn = view.findViewById(R.id.btnProfileCompleted)
        completeProfileCard = view.findViewById(R.id.CompleteProfile)
        orderHistoryButton = view.findViewById(R.id.orderHistoryExpandButton)
        myResponseButton = view.findViewById(R.id.MyResponseExpandButton)
        rootView = view.findViewById(R.id.rootView)
        profileMenu = view.findViewById(R.id.profileMenuButton)
        progressBar = view.findViewById(R.id.progressBarProfile)


        profileMenu.setOnClickListener {
            showPopUp(it)
        }

        getMyResponses()
        getProfileDetails()
        //check in this function if age.gender already there don't show complete profile card

        doneBtn.setOnClickListener {
            if (validateRestProfileDetail()) {
                completeProfileCard.visibility = View.GONE
                profileAgeGender.text = profileAgeE.text.toString().plus(", ")
                    .plus(profileGenderE.text.toString())
                profileInterest.text =
                    getString(R.string.interested_in).plus(profileInterestE.text.toString())
                userRef.update("dage", profileAgeE.text.toString())
                userRef.update("dgender", profileGenderE.text.toString())
                userRef.update("dinterests", profileInterestE.text.toString())
            }
        }

        myResponses.setOnClickListener {

            if (txtMyResponses.visibility == View.GONE) {
                //animation
                txtMyResponses.visibility = View.VISIBLE
                getMyResponses()
                myResponseButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            } else {
                txtMyResponses.visibility = View.GONE
                myResponseButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }

        }


        orderHistory.setOnClickListener {

            if (txtOrderHistory.visibility == View.GONE) {
                //animation

                txtOrderHistory.visibility = View.VISIBLE
                getOrderHistory()
                orderHistoryButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            } else {

                txtOrderHistory.visibility = View.GONE
                orderHistoryButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }

        }
        return view
    }

    private fun getMyResponses() {

        var result2: String

        if (user != null) {
            val myResponseCollRef = fStore.collection("Users")
                .document(user!!).collection("MyResponses")
            val jobRef = fStore.collection("JobQueryData")

            myResponseCollRef.get().addOnSuccessListener {
                // Log.i("TAG", "exists ${it.exists()}")

                if (it.documents.isNotEmpty()) {
                    txtMyResponses.text = ""
                    for (docRef in it) {
                        val modelData = docRef.toObject(MyResponsesData::class.java)

                        jobRef.document(modelData.reqId).get().addOnSuccessListener { reqdetails ->
                            val x = modelData.appliedTimestamp.toDate()
                            val sfd = SimpleDateFormat(
                                "dd-MMM-yyyy , K:mm a",
                                Locale.getDefault()
                            )
                            val dateSample = sfd.format(x)
                            Log.i("TAG", "MyResponse: DateSample $dateSample")

                            result2 = "ReqTitled: ${reqdetails.getString("djobTitle")} \n" +
                                    "ReqDate: ${reqdetails.getString("djobDate")}\n" +
                                    "AppliedAt: ${dateSample}\n\n"

                            // Log.i("TAG", "MyResponse: reqDetails $result2")

                            txtMyResponses.append(result2)
                        }

                    }
                } else {
                    Log.i("TAG", "MyResponses null")
                    txtMyResponses.text = getString(R.string.null_myresponses)

                }
                //else delete the deleted query from the list
            }.addOnFailureListener {
                Log.i("TAG", "data Retrieved error ${it.message}")
            }

        }

    }

    private fun getProfileDetails() {
        userRef.get().addOnSuccessListener {
            progressBar.visibility = View.GONE
            val name = it.get("dname").toString()
            Log.i("TAG", "name retrieved is $name")
            profileName.text = name
            val email = it.get("demail").toString()
            profileEmail.text = email
            profilePhone.text = it.getString("dphone")
            val age = it.getString("dage")
            val text = mContext.getString(R.string.interested_in)
            if (age != "") {
                profileAgeGender.text = age.plus(", ")
                    .plus(it.getString("dgender"))
                profileInterest.text = text.plus(it.getString("dinterests"))
            } else {
                completeProfileCard.visibility = View.VISIBLE
            }
        }
    }

    private fun getOrderHistory() {
        if (user != null) {
            userRef.get().addOnSuccessListener {


                @Suppress("UNCHECKED_CAST")
                val orderList: ArrayList<String> = it.get("dorderHistory") as ArrayList<String>
                //  Log.i("TAG", "OrderHistory returned value: $orderList")

                if (orderList.size == 0) {
                    Log.i("TAG", "RequestHistory null")
                    txtOrderHistory.text = getString(R.string.null_history)
                } else {
                    txtOrderHistory.text = ""
                }

                for (i in orderList) {
                    loadHistory(i)

//return will not work because the task is asynchronous i.e. returns value even before task is successful
                }

            }
        }
    }

    private fun loadHistory(order: String) {
        val orderRef = fStore.collection("JobQueryData").document(order)
        var result: String
        orderRef.get().addOnSuccessListener {
            // Log.i("TAG", "exists ${it.exists()}")

            if (it.exists()) {
                val modelData = it.toObject(FireStoreJobData::class.java)

                result =
                    "Title:  ${modelData!!.dJobTitle}  \nPayment:  ${modelData.dJobPayment}" +
                            " \nDescription: ${modelData.dJobDescription}\n\n"
                //  Log.i("TAG", "data Retrieved $result")
                txtOrderHistory.append(result)
            } else {

                userRef.update("dorderHistory", FieldValue.arrayRemove(order))
            }
            //else delete the deleted query from the list
        }.addOnFailureListener {
            Log.i("TAG", "data Retrieved error ${it.message}")

        }

    }

    private fun validateRestProfileDetail(): Boolean {
        var isValid = true
        //also validate if data already stored
        if (profileAgeE.text.isBlank()) {
            profileAgeE.error = getString(R.string.error_empty_age)
            isValid = false
        } else {
            profileAgeE.error = null
        }

        when {
            (profileGenderE.text.isBlank()) -> {
                profileGenderE.error = getString(R.string.error_empty_gender)
                isValid = false
            }
            ((profileGenderE.text.toString() != "Female") && (profileGenderE.text.toString() != "Male")
                    && (profileGenderE.text.toString() != "Other")) -> {
                Log.i("TAG", "ProfileFragment: gender ${profileGenderE.text}")
                profileGenderE.error = getString(R.string.error_gender)
                isValid = false
            }
            else -> {
                profileGenderE.error = null
            }
        }
        if (profileInterestE.text.isBlank()) {
            profileInterestE.error = getString(R.string.error_empty_interest)
            isValid = false
        } else {
            profileInterestE.error = null
        }

        return isValid
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //called for getString else gives error[Doubt:context not detached]
        mContext = context
        //  Log.i("TAG","Profile Fragment : onAttach called")
        //  Log.i("TAG","PF onAttach mContext===  $mContext")
    }

    private fun showPopUp(view: View) {
        val popup = PopupMenu(context, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.profile_menu, popup.menu)

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout -> {
                    Log.i("TAG", "profile: LogoutClicked")
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                    true
                }
                R.id.feedback -> {
                    Log.i("TAG", "profile: FeedBackClicked")
                    startActivity(Intent(activity, FeedbackActivity::class.java))
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

}

