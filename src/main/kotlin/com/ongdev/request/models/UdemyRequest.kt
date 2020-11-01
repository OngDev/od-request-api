package com.ongdev.request.models

import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name="udemy")
class UdemyRequest(
        var url: String = ""
) : BaseRequest()