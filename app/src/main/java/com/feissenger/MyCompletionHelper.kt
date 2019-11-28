package com.feissenger

import android.util.Log
import com.feissenger.data.DataRepository
import com.feissenger.data.api.model.ContactReadRequest
import com.feissenger.data.api.model.ContactReadResponse
import com.feissenger.data.api.model.RoomReadRequest
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.core.network.api.CompletionHandler
import com.giphy.sdk.core.network.response.MediaResponse
import kotlinx.coroutines.*

class MyCompletionHelper(
    val repository: DataRepository,
    val uid: String,
    val onError: (error: String) -> Unit,
    val contactReadResponse: ContactReadResponse
): CompletionHandler<MediaResponse> {

    override fun onComplete(result: MediaResponse?, e: Throwable?) {
        Log.i("","")
        GlobalScope.async { repository.saveMessage(onError,uid, contactReadResponse, result!!.data!!) }
    }
}