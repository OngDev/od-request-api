package com.ongdev.request.models.dtos.udemy

import com.ongdev.request.models.dtos.CreationTO

class UdemyRequestCreationTO(
        var title: String = "",
        var description: String = "",
        var url: String = ""
) : CreationTO