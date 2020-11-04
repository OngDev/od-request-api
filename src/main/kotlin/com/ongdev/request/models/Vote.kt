package com.ongdev.request.models

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
class Vote(
        @Id
        @GeneratedValue(generator = "uuid2")
        @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
        @Column(name = "id")
        var id: UUID? = null,
        var email: String,
        var isUp: Boolean = false
)