package com.ongdev.request.models.dtos

class UdemyRequestTO(var id: String?,
                     var title: String = "",
                     var description: String = "",
                     var isActive: Boolean = false,
                     var isArchived: Boolean = false,
                     var email: String = "",
                     var url: String = "",
                     var votes: List<VoteTO>? = null) : RequestTO