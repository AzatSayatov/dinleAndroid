package tm.bent.dinle.domain.model

data class Device(
    val id : String,
    val name : String,
    val os : String,
    val version : String,
    val lastActivity : String? = null,
)

data class DeviceResponse(
    val currentDevice: Device,
    val devices: List<Device>
)