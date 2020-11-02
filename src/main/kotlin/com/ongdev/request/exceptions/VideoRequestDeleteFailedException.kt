package com.ongdev.request.exceptions

import java.lang.RuntimeException

class VideoRequestDeleteFailedException(ex: Exception) : RuntimeException(ex)
