package uz.gita.homeworknotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MyBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("TTT", "Keldi Broadcast")
        Toast.makeText(context, "Broatcast", Toast.LENGTH_SHORT).show()
    }
}