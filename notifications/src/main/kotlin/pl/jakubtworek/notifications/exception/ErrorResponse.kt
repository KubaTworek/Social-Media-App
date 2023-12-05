package pl.jakubtworek.notifications.exception

data class ErrorResponse(
    val status: Int,
    val message: String?
)
