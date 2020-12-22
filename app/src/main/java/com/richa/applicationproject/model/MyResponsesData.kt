package com.richa.applicationproject.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class MyResponsesData(
    var reqId: String = "",
    var ownerId: String = "",
    @ServerTimestamp
    var appliedTimestamp: com.google.firebase.Timestamp = Timestamp(Date())
)
