package com.richa.applicationproject.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.richa.applicationproject.NotificationAdapter
import com.richa.applicationproject.R
import com.richa.applicationproject.model.NotificationData


/**
 *
 */
class NotificationFragment : Fragment() {
    private var fStore = FirebaseFirestore.getInstance()
    private var user = FirebaseAuth.getInstance().currentUser?.uid
    private var userRef1 = fStore.collection("Users")
    private var userRef2 = userRef1.document("$user")
    private lateinit var adapter: NotificationAdapter
    private lateinit var notificationRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var modelData: ArrayList<String> = ArrayList()
    private var notificationArray: ArrayList<NotificationData> = ArrayList()
    private lateinit var mContext: Context
    private lateinit var nullNoti: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notification, container, false)
        notificationRecyclerView = view.findViewById(R.id.notificationRecycler)
        progressBar = view.findViewById(R.id.progressBarNotify)
        nullNoti = view.findViewById(R.id.nullNotification)

        getNotificationList()

        return view
    }

    private fun getNotificationList() {
        Log.i("TAG", "getNotification Called")

        val responseList = userRef2.collection("Responder")
        // val query = responseList.orderBy(FieldPath.documentId())
        var username: String

        responseList.get().addOnSuccessListener {
            if (it.documents.isEmpty()) {
                Log.i("TAG", "dataRetrieved null : document Empty ")
                nullNoti.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            } else {
                @Suppress("UNCHECKED_CAST")
                for (docSnap in it) {
                    modelData = docSnap.get("dresponder") as ArrayList<String>
                    Log.i("TAG", "dataRetrieved  $modelData")

                    if(modelData.size!=0) {
                        nullNoti.visibility = View.GONE

                        for (respondee in modelData) {
                            userRef1.document(respondee).get().addOnSuccessListener { nameDoc ->
                                username = nameDoc.get("dname").toString()
                                //  Log.i("TAG", " notification User  $username")
                                val notificationDataObject =
                                    NotificationData(docSnap.id, username, respondee)
                                notificationArray.add(notificationDataObject)
                                val x = mContext.getString(R.string.has_responded_to_your_request)

                                //  Log.i("TAG", " notificationArray  $notificationArray")
                                if (activity != null) {
                                    adapter =
                                        NotificationAdapter(
                                            activity as Context,
                                            notificationArray,
                                            x
                                        )
                                    notificationRecyclerView.layoutManager =
                                        LinearLayoutManager(activity)
                                    notificationRecyclerView.adapter = adapter
                                    progressBar.visibility = View.GONE
                                }
                            }
                        }
                    }else{
                        Log.i("TAG", "dataRetrieved null : No response Received")
                        nullNoti.visibility = View.VISIBLE
                        nullNoti.text = getString(R.string.null_response)
                        progressBar.visibility = View.GONE
                    }
                }
            }
        }.addOnFailureListener {
            Log.i("TAG", "getNotification FailureListener ${it.message}")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

}