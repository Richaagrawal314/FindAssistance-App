package com.richa.applicationproject.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.richa.applicationproject.NotificationAdapter
import com.richa.applicationproject.R
import com.richa.applicationproject.model.NotificationData


/**
 * A simple [Fragment] subclass.
 * Use the [NotificationFragment] factory method to
 * create an instance of this fragment.
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notification, container, false)
        notificationRecyclerView = view.findViewById(R.id.notificationRecycler)
        progressBar = view.findViewById(R.id.progressBarNotify)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getNotificationList()
    }

    private fun getNotificationList() {
        val responseList = userRef2.collection("Responder")
        // val query = responseList.orderBy(FieldPath.documentId())
        var username: String


        responseList.get().addOnSuccessListener {
            @Suppress("UNCHECKED_CAST")
            for (docSnap in it) {
                modelData = docSnap.get("dresponder") as ArrayList<String>
                Log.i("TAG", "dataRetrieved  $modelData")

                for (respondee in modelData) {
                    //           Log.i("TAG", " notification respondee  $respondee")

                    userRef1.document(respondee).get()
                        .addOnSuccessListener { nameDoc ->
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
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }


}