package com.ongdev.request.models

import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name="udemy")
class UdemyRequest(
        title: String,
        description: String,
        var url: String = ""
) : BaseRequest(title = title, description = description)