package pl.jakubtworek.articles.exception

class UnauthorizedException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
