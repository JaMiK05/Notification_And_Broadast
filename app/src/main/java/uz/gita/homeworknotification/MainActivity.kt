package uz.gita.homeworknotification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import uz.gita.homeworknotification.ui.theme.HomeworkNotificationTheme

class MainActivity : ComponentActivity() {

    companion object {
        const val Chanel_ID = "notification"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeworkNotificationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Notification")
                }
            }
        }
    }

    private val myPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                createChannel(this@MainActivity)
            }
        }


    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier
                            .height(70.dp)
                            .width(150.dp),
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                if (ContextCompat.checkSelfPermission(
                                        this@MainActivity,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    )
                                    == PackageManager.PERMISSION_GRANTED
                                ) {
                                    createNotification(
                                        "My first notification",
                                        1,
                                        this@MainActivity
                                    )
                                } else {
                                    myPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            } else {
                                createChannel(this@MainActivity)
                                createNotification("My first notification", 1, this@MainActivity)
                            }
                        }
                    ) {
                        Text(text = name)
                    }
                }

            }

        }
    }

    private fun createNotification(s: String, i: Int, context: Context) {

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val broadcastIntent = Intent(this, MyBroadcast::class.java)
        val secondPendingIntent =
            PendingIntent.getBroadcast(this, 1, broadcastIntent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(
            context,
            Chanel_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("1- notification")
            .setContentText(s)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_launcher_foreground, "BroadCast", secondPendingIntent)

        NotificationManagerCompat.from(context).notify(i, notificationBuilder.build())

    }

    private fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val myChanel =
                NotificationChannel(Chanel_ID, "notify", importance)

            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(myChanel)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        HomeworkNotificationTheme {
            Greeting("Android")
        }
    }
}