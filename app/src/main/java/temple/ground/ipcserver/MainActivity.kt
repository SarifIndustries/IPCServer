package temple.ground.ipcserver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import temple.ground.ipcserver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = layoutInflater
        binding = ActivityMainBinding.inflate(inflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val client = RecentClient.clientInfo
        binding.connectionStatus.text =
            if (client == null) {
                binding.linearLayoutClientState.visibility = View.INVISIBLE
                getString(R.string.no_connected_client)
            } else {
                binding.linearLayoutClientState.visibility = View.VISIBLE
                getString(R.string.last_connected_client_info)
            }
        binding.txtPackageName.text = client?.clientPackageName
        binding.txtServerPid.text = client?.clientProcessId
        binding.txtData.text = client?.clientText
        binding.txtIpcMethod.text = client?.ipcMethod
    }
}