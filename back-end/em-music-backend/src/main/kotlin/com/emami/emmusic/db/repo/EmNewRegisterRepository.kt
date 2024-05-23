package com.emami.emmusic.db.repo

import com.emami.emmusic.db.model.EmNewRegister
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface EmNewRegisterRepository:JpaRepository<EmNewRegister,String> {
    fun findByPhoneNumber(phoneNumber: String): Optional<EmNewRegister>
}