package ssun.pe.kr.notificationexample

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FcmService"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "[x1210x] onCreate")
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Log.d(TAG, "[x1210x] onNewToken($token)")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "[x1210x] onMessageReceived()")

        remoteMessage?.let { message ->
            message.data.forEach {
                Log.d(TAG, "[x1210x] onMessageReceived(key = \"${it.key}\", value = \"${it.value}\")")
            }
        }
    }
}