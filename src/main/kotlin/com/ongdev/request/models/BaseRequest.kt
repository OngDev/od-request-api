package com.ongdev.request.models

import java.io.Serializable
import java.util.*
import javax.persistence.*

@MappedSuperclass
abstract class BaseRequest : Serializable {
    @Id
    @GeneratedValue
    private var id: Long? = null
    private lateinit var title: String
    private lateinit var description: String
    private var createdDate: Date? = null
    private var isActive: Boolean = false
    private var isArchived: Boolean = false
    private var archivedDate: Date? = null
    @OneToMany(cascade = [CascadeType.ALL])
    private var votes: MutableList<Vote> = mutableListOf()
}