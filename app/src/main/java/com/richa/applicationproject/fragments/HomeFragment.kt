package com.richa.applicationproject.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.richa.applicationproject.NoteAdapter
import com.richa.applicationproject.R
import com.richa.applicationproject.model.FireStoreJobData

class HomeFragment : Fragment() {
    private var fStore = FirebaseFirestore.getInstance()
    private var notebookRef = fStore.collection("JobQueryData")
    private lateinit var varAdapter: NoteAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var userName: TextView
    private lateinit var searchTags: EditText
    private lateinit var dashboardMenu: ImageButton
    private var user = FirebaseAuth.getInstance().currentUser?.uid
    private var userRef = fStore.document("Users/$user")
    private lateinit var searchString: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        dashboardMenu = view.findViewById(R.id.filter)
        recyclerView = view.findViewById(R.id.recyclerView)
        userName = view.findViewById(R.id.userName)
        searchTags = view.findViewById(R.id.searchTags)

        userRef.get().addOnSuccessListener {
            userName.text = it.get("dname").toString()
        }
        setUpRecyclerView(1)

        dashboardMenu.setOnClickListener {
            showPopUp(it)
        }

        searchTags.setOnEditorActionListener { v, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    searchString = searchTags.text.toString().trim()
                    setUpRecyclerView(2)
                    val imm: InputMethodManager =
                        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    true
                }
                else -> false
            }

        }


        return view
    }

    private fun showPopUp(view: View) {
        val popup = PopupMenu(context, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.dashboard_menu, popup.menu)

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.onlyUrgent -> {
                    setUpRecyclerView(3)
                    true
                }
                R.id.sortbydate -> {
                    setUpRecyclerView(1)
                    true
                }
                R.id.sortbyname -> {
                    setUpRecyclerView(4)
                    true
                }
                R.id.sortbypay -> {
                    setUpRecyclerView(5)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }


    private fun setUpRecyclerView(x: Int) {
        //all query
        // val query2=notebookRef.orderBy(FieldPath.documentId())

        val query: Query
        when (x) {
            1 -> {
                //sortByDate
                query = notebookRef.orderBy("djobDate", Query.Direction.DESCENDING)
            }
            2 -> {
                //Tag search
                query = notebookRef.whereArrayContains("djobTags", searchString)
                query.get().addOnSuccessListener {
//                    Log.i("TAG", "check Empty ${it.documents.isEmpty()}")
//                    Log.i("TAG", "check Empty ${it.isEmpty}")
                    if (it.documents.isEmpty()) {
                        Toast.makeText(context, "No Results Found.Enter Only LowerCase.",
                            Toast.LENGTH_LONG).show()
                    }
                }
            }
            3 -> {
                //OnlyUrgentFilter
                query = notebookRef.whereEqualTo("durgent", true)
            }
            4 -> {
                //sortByTitle
                query = notebookRef.orderBy("djobTitle", Query.Direction.ASCENDING)
            }
            5 -> {
                //sortByPayment[doesn't work payment String in firestore]
                query = notebookRef.orderBy("djobPayment", Query.Direction.DESCENDING)
            }
            else -> query = notebookRef.orderBy("djobDate", Query.Direction.DESCENDING)
        }


        val options = FirestoreRecyclerOptions.Builder<FireStoreJobData>()
            .setQuery(query, FireStoreJobData::class.java).setLifecycleOwner(this).build()

        varAdapter = NoteAdapter(activity as Context, options)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = varAdapter

        //    Log.i("TAG", "home Fragment searchTags ${searchTags.text}")

    }

    override fun onStart() {
        super.onStart()
        varAdapter.startListening()
        Log.i("TAG", "onStart home")
    }

    override fun onStop() {
        super.onStop()
        varAdapter.stopListening()
        Log.i("TAG", "onStop home")
    }
}