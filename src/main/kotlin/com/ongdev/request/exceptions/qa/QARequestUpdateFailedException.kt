package com.ongdev.request.exceptions.qa

import java.lang.RuntimeException

class QARequestUpdateFailedException(
        message: String? = "Failed to update this QnA request",
        ex: Exception? = null) : RuntimeException(message, ex)