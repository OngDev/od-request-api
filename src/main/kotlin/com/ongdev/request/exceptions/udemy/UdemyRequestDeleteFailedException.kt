package com.ongdev.request.exceptions.udemy

import java.lang.RuntimeException

class UdemyRequestDeleteFailedException(ex: Exception) : RuntimeException(ex)