package com.emami.emmusic.security.repo

import com.emami.emmusic.security.model.EmAuthority
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface EmAuthorityRepository : JpaRepository<EmAuthority, Long> {
    fun findByName(name: String): Optional<EmAuthority>
}