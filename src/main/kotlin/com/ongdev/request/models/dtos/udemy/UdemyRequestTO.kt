package com.ongdev.request.models.dtos.udemy

import com.ongdev.request.models.dtos.RequestTO
import com.ongdev.request.models.dtos.VoteTO

class UdemyRequestTO(var id: String,
                     var title: String,
                     var description: String,
                     var isActive: Boolean,
                     var isArchived: Boolean,
                     var email: String,
                     var url: String,
                     var votes: List<VoteTO>) : RequestTO