package temple.ground.ipcserver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class IPCBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        RecentClient.clientInfo = ClientInfo(
            intent?.getStringExtra(PACKAGE_NAME),
            intent?.getStringExtra(PID) ?: "No pid",
            intent?.getStringExtra(DATA),
            "Broadcast"
        )
    }
}