package com.ongdev.request.models.dtos.video

import com.ongdev.request.models.dtos.RequestTO
import com.ongdev.request.models.dtos.VoteTO

class VideoRequestTO(var id: String,
                     var title: String,
                     var description: String,
                     var isActive: Boolean,
                     var isArchived: Boolean,
                     var email: String,
                     var votes: List<VoteTO>) : RequestTO
