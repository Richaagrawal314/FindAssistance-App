package com.richa.applicationproject

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.richa.applicationproject.fragments.QueryDialogFragment
import com.richa.applicationproject.model.FireStoreJobData

class NoteAdapter(val context: Context, options: FirestoreRecyclerOptions<FireStoreJobData>) :
    FirestoreRecyclerAdapter<FireStoreJobData, NoteAdapter.NoteHolder>(options) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_job_list, parent, false)

        return NoteHolder(view)
    }

    override fun getItemCount(): Int {
        //  val x = snapshots.isEmpty()
        //   Log.i("TAG", "NoteAdapter:snapshotSize getItemCount is $x")
        return snapshots.size
    }


    override fun onBindViewHolder(holder: NoteHolder, p1: Int, model: FireStoreJobData) {

        val id = snapshots.getSnapshot(p1).id

        model.documentId = id
        //   Log.i("TAG", "NoteAdapter: id is $id  and ${model.documentId}  and ${model.dJobTitle}")
        holder.apply {
            addUrgent.visibility = View.GONE
            tag1.visibility = View.GONE
            tag2.visibility = View.GONE
            tag3.visibility = View.GONE
            tag4.visibility = View.GONE
            tag5.visibility = View.GONE
            jobTitle.text = model.dJobTitle
            jobDate.text = model.dJobDate
            jobPayment.text = model.dJobPayment
            if (model.dUrgent) {
                addUrgent.visibility = View.VISIBLE
            }
            val tagSize = model.dJobTags.size
            Log.i("TAG", "NoteAdapter: id is $id  and tagArraySize $tagSize ")
            if (tagSize > 0) {
                tag1.text = model.dJobTags[0]
                tag1.visibility = View.VISIBLE
            }
            if (tagSize > 1) {
                tag2.text = model.dJobTags[1]
                tag2.visibility = View.VISIBLE
            }
            if (tagSize > 2) {
                tag3.text = model.dJobTags[2]
                tag3.visibility = View.VISIBLE
            }
            if (tagSize > 3) {
                tag4.text = model.dJobTags[3]
                tag4.visibility = View.VISIBLE
            }
            if (tagSize > 4) {
                tag5.text = model.dJobTags[4]
                tag5.visibility = View.VISIBLE
            }


            queryCard.setOnClickListener {
                Log.i("TAG", "NoteAdapter: queryCard Called")
                val activity = context as FragmentActivity
                val fm = activity.supportFragmentManager
                val dialog = QueryDialogFragment(model)

                dialog.show(fm, "MyCustomDialog")
            }
        }
    }

    class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val jobTitle: TextView = itemView.findViewById(R.id.jobTitle)
        val jobDate: TextView = itemView.findViewById(R.id.jobDate)
        val jobPayment: TextView = itemView.findViewById(R.id.jobPayment)
        val addUrgent: ImageButton = itemView.findViewById(R.id.imgBtnAddUrgent)
        val tag1: TextView = itemView.findViewById(R.id.tagBox1)
        val tag2: TextView = itemView.findViewById(R.id.tagBox2)
        val tag3: TextView = itemView.findViewById(R.id.tagBox3)
        val tag4: TextView = itemView.findViewById(R.id.tagBox4)
        val tag5: TextView = itemView.findViewById(R.id.tagBox5)
        val queryCard: RelativeLayout = itemView.findViewById(R.id.queryCard)
    }

}