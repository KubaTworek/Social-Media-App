package pl.jakubtworek.articles.exception

class AuthorApiException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
