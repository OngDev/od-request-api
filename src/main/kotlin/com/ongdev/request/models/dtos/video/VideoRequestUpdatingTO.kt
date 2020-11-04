package com.ongdev.request.models.dtos.video

import com.ongdev.request.models.dtos.UpdatingTO

class VideoRequestUpdatingTO (
        var title: String = "",
        var description: String = ""
) : UpdatingTO