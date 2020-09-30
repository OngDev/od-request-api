package com.ongdev.request.repositories

import com.ongdev.request.models.Vote
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VoteRepository : CrudRepository<Vote, UUID>