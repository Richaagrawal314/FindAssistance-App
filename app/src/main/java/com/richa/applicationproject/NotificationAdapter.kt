package com.richa.applicationproject

import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.richa.applicationproject.fragments.ResponderDialogFragment
import com.richa.applicationproject.model.NotificationData

class NotificationAdapter(
    var context: Context,
    private var notifyArray: ArrayList<NotificationData>,
    private var x: String
) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private var fStore = FirebaseFirestore.getInstance()

    //    private var userRef1 = fStore.collection("Users")
    private var jobRef = fStore.collection("JobQueryData")

    class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var responseName: TextView = view.findViewById(R.id.txtResponseName)
        var contactResponse: TextView = view.findViewById(R.id.ContactYourResponse)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.single_notification_response, parent, false)
        Log.i("TAG", "Create ViewHolder called")
        return NotificationViewHolder(view)
    }


//    override fun onBindViewHolder(holder: NotificationViewHolder, p1: Int, model: ResponderData) {
//        val id = snapshots.getSnapshot(p1).id
//        var name: String
//        if (model.dResponder.isNotEmpty()) {
//            for (listData in model.dResponder) {
//                Log.i("TAG", "listData is $listData")
//                userRef1.document(listData).get()
//                    .addOnSuccessListener { nameDoc ->
//                        name = nameDoc.get("dname").toString()
//                        val nameStyled = "<i><font color='#2BCC6F'>$name</font></i>"
//                        val reqStyled = "<i><font color='#2BCC6F'>$id</font></i>"
//                        val xx = "<font color='#000000'>$x</font>"
//                        val responseTxt = Html.fromHtml(
//                            nameStyled
//                                .plus(xx).plus(reqStyled)
//                        )
//                        Log.i("TAG", "name is $name")
//                        Log.i("TAG", "responseTextStyled is $responseTxt")
//                        holder.responseName.text = responseTxt
//                    }
//            }
//
//            holder.contactResponse.setOnClickListener {
//                Toast.makeText(context, "You'll be connected shortly", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val model = notifyArray[position]
        val name = notifyArray[position].responderName
        val id = notifyArray[position].reqId
        var reqname: String
        jobRef.document(id).get().addOnSuccessListener { docname ->
            reqname = docname.get("djobTitle").toString()
            Log.i("TAG", " notification req  $reqname")
            val nameStyled = "<i><font color='#2BCC6F'>$name</font></i>"
            val reqStyled = "<i><font color='#2BCC6F'>$reqname</font></i>"
            val xx = "<font color='#000000'>$x</font>"
            @Suppress("DEPRECATION") val responseTxt = Html.fromHtml(
                nameStyled
                    .plus(xx).plus(reqStyled)
            )

            //  Log.i("TAG", "responseTextStyled is $responseTxt")
            holder.responseName.text = responseTxt

            holder.contactResponse.setOnClickListener {

                Log.i("TAG", "NotificationAdapter: notificationCard Called")
                val activity = context as FragmentActivity
                val fm = activity.supportFragmentManager
                val dialog = ResponderDialogFragment(model)
                dialog.show(fm, "MyCustomDialog")
                // Toast.makeText(context, "You'll be connected shortly", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return notifyArray.size
    }


}