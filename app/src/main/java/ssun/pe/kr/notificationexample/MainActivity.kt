package ssun.pe.kr.notificationexample

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {
    companion object {
        private const val CHANNEL_ID = "CHANNEL_1"
        private const val PROGRESS_DURATION: Long = 3000
    }

    private var mNotiCount = 0
    private var mProgressShow = false

    private val mNotificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    // 알림 클릭해서 들어오는 경우 data가 intent에 들어있음
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // 단순 알림 (업데이트)
        btnNotify.setOnClickListener { showSimpleNotification() }
        // 확장 레이아웃 (인박스 스타일)
        btnNotify2.setOnClickListener { showExtendedNotification() }
        // 그룹
        btnNotify2_2.setOnClickListener { showGroupNotification() }
        // 프로그레스
        btnNotify3.setOnClickListener { showProgressNotification(PROGRESS_DURATION) }
        // 프로그레스 (무한)
        btnNotify4.setOnClickListener {
            toggleInfiniteProgressNotification(!mProgressShow)
            mProgressShow = !mProgressShow
        }
        // 사용자 지정 알림 레이아웃
        btnNotify5.setOnClickListener { showCustomViewNotification() }
        // 빅픽쳐 스타일
        btnNotify6.setOnClickListener { showBigPictureNotification() }
        // 빅텍스트 스타일
        btnNotify7.setOnClickListener { showBigTextNotification() }
        // 메시징 스타일
        btnNotify8.setOnClickListener { showMessagingNotification() }
        // TODO : DecoratedCustomViewStyle
        // TODO : 액션

        btnSetting.setOnClickListener {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            //intent.putExtra(Settings.EXTRA_CHANNEL_ID, CHANNEL_ID)
            startActivity(intent)
        }
    }

    private fun showSimpleNotification() {
        mNotiCount++

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_active)
                .setContentTitle(if (mNotiCount == 1) "단순 알림" else "알림 업데이트")
                .setContentText("텍스트 $mNotiCount")
                .setColor(ContextCompat.getColor(this, R.color.red_500))

        mNotificationManager.notify(0, builder.build())
    }

    private fun showExtendedNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_active)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_notifications_active)) // 벡터 노노
                .setContentTitle("확장 레이아웃")
                .setContentText("텍스트2")
                .setColor(ContextCompat.getColor(this, R.color.orange_500))

        val inboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.setBigContentTitle("어쩌고 저쩌고")
        arrayOf("NC vs 두산", "롯데 vs 한화", "삼성 vs SK", "kt vs 넥센", "LG vs KIA").forEach { line ->
            inboxStyle.addLine(line)
        }
        inboxStyle.setSummaryText("5 경기가 랄랄라")
        builder.setStyle(inboxStyle)

        mNotificationManager.notify(1, builder.build())
    }

    private fun showGroupNotification() {
        // 그룹 알림을 먼저 생성
        val groupBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_active)
                .setContentTitle("알림")
                .setContentText("텍스트")
                .setColor(ContextCompat.getColor(this, R.color.teal_500))
                .setGroup("NOTI_GROUP")
                .setGroupSummary(true)

        mNotificationManager.notify(22000, groupBuilder.build())

        // 자식 알림들을 생성
        for (i in 1 .. 5) {
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notifications_active)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_notifications_active)) // 벡터 노노
                    .setContentTitle("알림 $i")
                    .setContentText("텍스트 $i")
                    .setColor(ContextCompat.getColor(this, R.color.teal_500))
                    .setGroup("NOTI_GROUP")

            mNotificationManager.notify(22000 + i, builder.build())
        }
    }

    private fun showProgressNotification(delay: Long) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_active)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_notifications_active)) // 벡터 노노
                .setContentTitle("프로그레스")
                .setColor(ContextCompat.getColor(this, R.color.yellow_500))

        launch {
            for (i in 0 .. 100) {
                builder.setProgress(100, i, false)
                mNotificationManager.notify(3, builder.build())

                delay(delay / 100)
            }

            delay(1000)

            builder.setContentText("Completed").setProgress(0, 0, false)
            mNotificationManager.notify(3, builder.build())
        }
    }

    private fun toggleInfiniteProgressNotification(show: Boolean) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_active)
                .setContentTitle("프로그레스 (무한)")
                .setContentText(if (show) null else "Completed")
                .setColor(ContextCompat.getColor(this, R.color.green_500))
                .setProgress(0, 0, show)

        mNotificationManager.notify(3, builder.build())
    }

    private fun showCustomViewNotification() {
        val remoteViews = RemoteViews(BuildConfig.APPLICATION_ID, R.layout.custom_notification)
        remoteViews.setImageViewResource(R.id.ivImage, R.drawable.ic_notifications_active)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_active)
                .setContentTitle("커스텀")
                .setContentText("텍스트5")
                .setColor(ContextCompat.getColor(this, R.color.blue_500))
                .setContent(remoteViews)

        mNotificationManager.notify(4, builder.build())
    }

    private fun showBigPictureNotification() {
        Glide.with(this)
                .asBitmap()
                .load("https://images.pexels.com/photos/1268121/pexels-photo-1268121.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
                .into(object: SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val builder = NotificationCompat.Builder(this@MainActivity, CHANNEL_ID)
                                .setContentTitle("빅픽쳐 스타일")
                                .setContentText("빅픽쳐 스타일")
                                .setSmallIcon(R.drawable.ic_notifications_active)
                                .setLargeIcon(resource)
                                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                                .setColor(ContextCompat.getColor(this@MainActivity, R.color.indigo_500))

                        mNotificationManager.notify(5, builder.build())
                    }
                })
    }

    private fun showBigTextNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("빅텍스트 스타일")
                .setContentText("빅텍스트 스타일")
                .setSmallIcon(R.drawable.ic_notifications_active)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_notifications_active))
                .setStyle(NotificationCompat.BigTextStyle().bigText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."))
                .setColor(ContextCompat.getColor(this, R.color.purple_500))

        mNotificationManager.notify(6, builder.build())
    }

    private fun showMessagingNotification() {
        val time = System.currentTimeMillis()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("메시징 스타일")
                .setContentText("메시징 스타일")
                .setSmallIcon(R.drawable.ic_notifications_active)
                .setStyle(NotificationCompat.MessagingStyle("백선철")
                        .addMessage("메시지1", time, "백선철")
                        .addMessage("메시지3", time + 2, "백선철")
                        .addMessage("메시지2", time + 1, "백선철")
                        .addMessage("메시지4", time + 3, "백선철"))
                .setColor(ContextCompat.getColor(this, R.color.red_500))

        mNotificationManager.notify(7, builder.build())
    }
}
