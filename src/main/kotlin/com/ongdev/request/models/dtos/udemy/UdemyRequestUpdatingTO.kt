package com.ongdev.request.models.dtos.udemy

import com.ongdev.request.models.dtos.UpdatingTO

class UdemyRequestUpdatingTO (
        var title: String = "",
        var description: String = "",
        var url: String = ""
) : UpdatingTO