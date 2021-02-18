package com.ongdev.request.models

import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import java.util.*
import javax.persistence.*

@MappedSuperclass
abstract class BaseRequest(
        @Id
        @GeneratedValue(generator = "uuid2")
        @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
        @Column(name = "id")
        var id: UUID? = null,
        @Column(nullable = false)
        var title: String = "",
        @Lob
        @Column(nullable = false)
        var description: String = "",
        var createdDate: Date? = null,
        var isActive: Boolean = false,
        var isArchived: Boolean = false,
        var archivedDate: Date? = null,
        @Column(nullable = false)
        var email: String = "",
        @OneToMany(cascade = [CascadeType.ALL])
        var votes: MutableList<Vote> = mutableListOf()
) : Serializable