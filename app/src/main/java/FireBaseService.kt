import android.util.Log
import com.feissenger.data.DataRepository.Companion.TAG
import com.google.firebase.messaging.FirebaseMessagingService

class FireBaseService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }
}