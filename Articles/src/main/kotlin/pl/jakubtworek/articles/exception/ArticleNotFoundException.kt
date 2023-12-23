package pl.jakubtworek.articles.exception

class ArticleNotFoundException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
