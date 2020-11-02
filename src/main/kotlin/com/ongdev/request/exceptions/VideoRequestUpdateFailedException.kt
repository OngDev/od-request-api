package com.ongdev.request.exceptions

import java.lang.RuntimeException

class VideoRequestUpdateFailedException(
        message: String? = "Failed to update this Video request",
        ex: Exception? = null) : RuntimeException(message, ex)
