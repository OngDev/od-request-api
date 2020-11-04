package com.ongdev.request.models.dtos.qna

import com.ongdev.request.models.dtos.UpdatingTO

class QARequestUpdatingTO (
        var title: String = "",
        var description: String = ""
) : UpdatingTO