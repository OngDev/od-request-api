package com.ongdev.request.models.dtos.video

import com.ongdev.request.models.dtos.CreationTO

class VideoRequestCreationTO(
        var title: String = "",
        var description: String = ""
) : CreationTO