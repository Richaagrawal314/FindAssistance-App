package com.richa.applicationproject.model

import com.google.firebase.firestore.ServerTimestamp

data class MyResponsesData(
    var reqId: String = "",
    var ownerId: String = "",
    @ServerTimestamp
    var appliedTimestamp: com.google.firebase.Timestamp
)
