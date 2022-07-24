package temple.ground.ipcserver

data class ClientInfo(
    var clientPackageName: String?,
    var clientProcessId: String,
    var clientText: String?,
    var ipcMethod: String
)