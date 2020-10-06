package com.ongdev.request.utils

import com.fasterxml.jackson.databind.ObjectMapper
import java.lang.Exception
import java.lang.RuntimeException

class JsonUtil {
    companion object {
        fun asJsonString(obj: Any): String {
            try {
                return ObjectMapper().writeValueAsString(obj)
            } catch (ex: Exception) {
                throw RuntimeException(ex)
            }
        }
    }
}