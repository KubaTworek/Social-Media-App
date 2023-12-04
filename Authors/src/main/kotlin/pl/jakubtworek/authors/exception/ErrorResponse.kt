package pl.jakubtworek.authors.exception

data class ErrorResponse(
    val status: Int,
    val message: String?
)
