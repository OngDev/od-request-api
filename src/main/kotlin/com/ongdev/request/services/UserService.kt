package com.ongdev.request.services

import com.ongdev.request.models.auth.UserInfo

interface UserService {
    fun getUserInfoFromToken(token: String?) : UserInfo?
}