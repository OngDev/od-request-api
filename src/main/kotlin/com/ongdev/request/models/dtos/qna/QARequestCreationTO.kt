package com.ongdev.request.models.dtos.qna

import com.ongdev.request.models.dtos.CreationTO

class QARequestCreationTO(
        var title: String = "",
        var description: String = ""
) : CreationTO