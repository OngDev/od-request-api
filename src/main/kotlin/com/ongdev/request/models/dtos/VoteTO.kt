package com.ongdev.request.models.dtos

import java.util.*

class VoteTO(
         var id: UUID? = null,
         var email: String = "",
         var isUp : Boolean = false
)