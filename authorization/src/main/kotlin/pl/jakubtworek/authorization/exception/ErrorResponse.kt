package pl.jakubtworek.authorization.exception

data class ErrorResponse(
    val status: Int,
    val message: String?
)
