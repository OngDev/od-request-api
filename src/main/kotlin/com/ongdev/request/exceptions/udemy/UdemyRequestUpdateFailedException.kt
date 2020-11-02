package com.ongdev.request.exceptions.udemy

import java.lang.RuntimeException

class UdemyRequestUpdateFailedException(
        message: String? = "Failed to update this Udemy request",
        ex: Exception? = null) : RuntimeException(message, ex)
