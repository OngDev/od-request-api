package com.ongdev.request.models.dtos.qna

import com.ongdev.request.models.dtos.RequestTO
import com.ongdev.request.models.dtos.VoteTO

class QARequestTO(var id: String,
                  var title: String,
                  var description: String,
                  var isActive: Boolean,
                  var isArchived: Boolean,
                  var email: String,
                  var votes: List<VoteTO>) : RequestTO