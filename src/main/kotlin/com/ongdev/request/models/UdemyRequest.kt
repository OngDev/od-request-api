package com.ongdev.request.models

import javax.persistence.Entity

@Entity
class UdemyRequest(
        val url: String
) : BaseRequest()