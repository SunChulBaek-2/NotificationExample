package ssun.pe.kr.notificationexample

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {

    companion object {
        const val TAG = "FcmService"

        const val KEY_FCM_TOKEN = "KEY_FCM_TOKEN"
    }

    var token: String?
        get() = getSharedPreferences(TAG, Context.MODE_PRIVATE).getString(KEY_FCM_TOKEN, null)
        set(token) {
            getSharedPreferences(TAG, Context.MODE_PRIVATE).edit().putString(KEY_FCM_TOKEN, token).apply()
        }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "[x1210x] onCreate")
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)

        if (token != this.token) {
            this.token = token
        }
        Log.d(TAG, "[x1210x] onNewToken( token = ${this.token} )")
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