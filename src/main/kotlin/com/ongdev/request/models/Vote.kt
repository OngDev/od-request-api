package com.ongdev.request.models

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
class Vote {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(32)")
    private var id: UUID? = null
    private lateinit var email: String
    private var isUp : Boolean = false
}