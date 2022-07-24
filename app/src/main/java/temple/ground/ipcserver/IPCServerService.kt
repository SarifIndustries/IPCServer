package temple.ground.ipcserver

import android.app.Service
import android.content.Intent
import android.os.*
class IPCServerService : Service() {

    companion object {
        var connectionCount: Int = 0
    }

    val binder: IBinder = object : IPalisadeAIDL.Stub() {
        override fun getPid(): Int = Process.myPid()

        override fun getConnectionCount(): Int = IPCServerService.connectionCount

        override fun setDisplayedValue(packageName: String?, pid: Int, text: String?) {
            val clientText = text ?: "No text"
            RecentClient.clientInfo = ClientInfo(
                clientPackageName = packageName ?: "No p name",
                clientProcessId = pid.toString(),
                clientText = clientText,
                ipcMethod = "AIDL"
            )
        }
    }

    override fun onBind(intent: Intent?): IBinder? =
        when (intent?.action) {
            "aidl_palisade" -> binder
            "messenger_palisade" -> mMessenger.binder
            else -> null
        }.also { connectionCount++ }

    // Messenger IPC - Messenger object contains binder to send to client
    private val mMessenger = Messenger(IncomingHandler())

    // Messenger IPC - Message Handler
    internal inner class IncomingHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            // Get message from client. Save recent connected client info.
            val receivedBundle = msg.data
            RecentClient.clientInfo = ClientInfo(
                receivedBundle.getString(PACKAGE_NAME),
                receivedBundle.getInt(PID).toString(),
                receivedBundle.getString(DATA),
                "Messenger"
            )

            // Send message to the client. The message contains server info
            val message = Message.obtain(this@IncomingHandler, 0)
            val bundle = Bundle()
            bundle.putInt(CONNECTION_COUNT, connectionCount)
            bundle.putInt(PID, Process.myPid())
            message.data = bundle
            // The service can save the msg.replyTo object as a local variable
            // so that it can send a message to the client at any time
            msg.replyTo.send(message)
        }
    }
}

