package pl.jakubtworek.articles.exception

class AuthorizationApiException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
