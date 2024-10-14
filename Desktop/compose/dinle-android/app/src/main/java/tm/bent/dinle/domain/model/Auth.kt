package tm.bent.dinle.domain.model

data class Auth(
    val phone: String,
    val otp: Int? = null,
    val device: Device? = null
)

data class Token(
    val refresh: String,
    val access: String,
)

