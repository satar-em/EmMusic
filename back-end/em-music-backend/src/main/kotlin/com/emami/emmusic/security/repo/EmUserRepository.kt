package com.emami.emmusic.security.repo

import com.emami.emmusic.security.model.EmUser
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface EmUserRepository : JpaRepository<EmUser, Long> {
    fun findByUsername(username: String): Optional<EmUser>
    fun findByPhoneNumber(phoneNumber: String): Optional<EmUser>
    fun findByEmail(email: String): Optional<EmUser>
    fun findByEmailAndPhoneNumber(email: String, phoneNumber: String): Optional<EmUser>

}