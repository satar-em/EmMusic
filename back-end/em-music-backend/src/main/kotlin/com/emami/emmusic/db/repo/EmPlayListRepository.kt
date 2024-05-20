package com.emami.emmusic.db.repo

import com.emami.emmusic.db.model.EmPlaylist
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface EmPlayListRepository : JpaRepository<EmPlaylist, Long> {
    fun findByName(name: String): Optional<EmPlaylist>
}