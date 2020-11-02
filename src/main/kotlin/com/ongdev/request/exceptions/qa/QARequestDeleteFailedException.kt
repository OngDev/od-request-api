package com.ongdev.request.exceptions.qa

import java.lang.RuntimeException

class QARequestDeleteFailedException(
        message: String? = "Failed to delete QnA request",
        ex: Exception? = null) : RuntimeException(message, ex)
