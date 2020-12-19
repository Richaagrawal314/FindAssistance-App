package com.richa.applicationproject.model

import com.google.firebase.firestore.Exclude

data class FireStoreJobData(
    var dJobTitle: String = "",
    var dJobPayment: String = "",
    var dNegotiable: Boolean = false,
    var dJobDate: String = "",
    var dJobDescription: String = "",
    var dUrgent: Boolean = false,
    var dUserId: String = "",
    var dJobTags: ArrayList<String> = ArrayList()
) {
    var documentId: String = ""
        @Exclude get   //a field which is not to be added in firestore
}