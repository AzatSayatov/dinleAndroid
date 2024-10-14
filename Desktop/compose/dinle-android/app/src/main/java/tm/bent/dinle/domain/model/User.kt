package tm.bent.dinle.domain.model

data class User(
    val userId: String,
    val phone: String,
    val notifications: Boolean,
    val token: String,
    val refreshToken: String,
    val createdAt: String = "",

    )
