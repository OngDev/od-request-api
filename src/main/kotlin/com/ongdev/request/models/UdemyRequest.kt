package com.ongdev.request.models

import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name="udemy")
class UdemyRequest(
        val url: String
) : BaseRequest()