package com.ongdev.request.models.mappers

import com.ongdev.request.models.Vote
import com.ongdev.request.models.dtos.VoteTO

fun Vote.toVoteTO() = VoteTO(
        id, email, isUp
)

fun VoteTO.toVote() = Vote(
        id, email, isUp
)