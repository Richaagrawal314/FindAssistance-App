package com.richa.applicationproject.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.richa.applicationproject.R
import com.richa.applicationproject.databinding.ActivityFeedbackBinding


class FeedbackActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var feedbackBinding: ActivityFeedbackBinding
    private var feedbackRating: Int = 0
    private var feedbackEmailErr: Boolean = true
    private var ratingBtnList: ArrayList<ImageButton>? = null
    private var feedbackMsgErr: Boolean = true
    private var feedbackCategory: String = ""
    lateinit var emailFB: String
    lateinit var messageFB: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_feedback)
        feedbackBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_feedback)
        val email = feedbackBinding.FeedbackpageEmail
        val message = feedbackBinding.FeedbackPageMessage

        //init email ID input field
        email.isErrorEnabled = true
        email.error = getString(R.string.email_error)
        email.editText?.addTextChangedListener(object :
            TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                emailFB = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!email.editText?.text.isNullOrEmpty() && !isValidEmail(s!!)
                ) {
                    email.isErrorEnabled = true
                    email.error = getString(R.string.valid_error)
                    feedbackEmailErr = true
                } else {
                    if (s.toString().isEmpty()) {
                        email.isErrorEnabled = true
                        email.error = getString(R.string.email_error)
                        feedbackEmailErr = true
                    } else {
                        email.isErrorEnabled = false
                        feedbackEmailErr = false

                    }
                }
            }

        })

        //init message input field
        message.isErrorEnabled = true
        message.error = getString(R.string.msg_error)
        message.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                messageFB=s.toString()

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length > message.counterMaxLength) {
                    message.isErrorEnabled = true
                    message.error = getString(R.string.char_error)
                    feedbackMsgErr = true
                } else {
                    if (s.toString().isEmpty()) {
                        message.isErrorEnabled = true
                        message.error = getString(R.string.msg_error)
                        feedbackMsgErr = true
                    } else {
                        message.isErrorEnabled = false
                        feedbackMsgErr = false
                    }
                }
            }
        })

        //init ratingBtnList, an arraylist of ImageButtons, it is going to be used to add rating and change image resource, see fun assignRating()
        ratingBtnList = ArrayList()
        ratingBtnList?.add(feedbackBinding.FeedbackpageS1)
        ratingBtnList?.add(feedbackBinding.FeedbackpageS2)
        ratingBtnList?.add(feedbackBinding.FeedbackpageS3)
        ratingBtnList?.add(feedbackBinding.FeedbackpageS4)
        ratingBtnList?.add(feedbackBinding.FeedbackpageS5)
        //set the onclick listener for each of the star buttons
        for (i in ratingBtnList!!) {
            i.setOnClickListener(this)
        }

        //set click listeners for category radio buttons
        feedbackBinding.FeedbackpageRd1.setOnClickListener(this)
        feedbackBinding.FeedbackpageRd2.setOnClickListener(this)
        feedbackBinding.FeedbackpageRd3.setOnClickListener(this)
        //init category = "bug"
        feedbackCategory = getString(R.string.feedback_bug)

        //Final submit button
        feedbackBinding.FeedbackpageSendbtn.setOnClickListener {
            if (!feedbackEmailErr && !feedbackMsgErr && feedbackRating != 0) {
                Toast.makeText(this, getString(R.string.feedback_thank), Toast.LENGTH_SHORT).show()
                //add backend code for adding rating, category, message, email ID
              Log.i("TAG","Feedback: email $emailFB \n message $messageFB \n rating $feedbackRating")

            } else {
                if (feedbackMsgErr || feedbackEmailErr || feedbackRating == 0) {
                    Toast.makeText(this, getString(R.string.input), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    //fun used to validate email ID
    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.FeedbackpageS1 -> {
                assignRating(1)
            }
            R.id.FeedbackpageS2 -> {
                assignRating(2)
            }
            R.id.FeedbackpageS3 -> {
                assignRating(3)
            }
            R.id.FeedbackpageS4 -> {
                assignRating(4)
            }
            R.id.FeedbackpageS5 -> {
                assignRating(5)
            }
            R.id.FeedbackpageRd1 -> {
                if (feedbackBinding.FeedbackpageRd1.isChecked) {
                    feedbackCategory = feedbackBinding.FeedbackpageRd1.text.toString()
                }

            }
            R.id.FeedbackpageRd2 -> {
                if (feedbackBinding.FeedbackpageRd2.isChecked) {
                    feedbackCategory = feedbackBinding.FeedbackpageRd2.text.toString()
                }
            }
            R.id.FeedbackpageRd3 -> {
                if (feedbackBinding.FeedbackpageRd3.isChecked) {
                    feedbackCategory = feedbackBinding.FeedbackpageRd3.text.toString()
                }
            }
        }

    }

    //fun used to assign rating and change image resource of star image buttons
    private fun assignRating(starIndex: Int) {
        Log.i("TAG", "Feedback: feedbackRating $feedbackRating")
        if (feedbackRating != starIndex) {

            for (i in 0 until feedbackRating) {
                ratingBtnList?.get(i)?.setImageResource(R.drawable.ic_star_border_black_24dp)
            }
            for (i in 0 until starIndex) {
                ratingBtnList?.get(i)?.setImageResource(R.drawable.ic_star_black_24dp)
            }
            feedbackRating = starIndex
        }
    }


}