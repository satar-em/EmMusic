package com.emami.emmusic.security.init

import com.emami.emmusic.security.model.EmAuthority
import com.emami.emmusic.security.model.EmUser
import com.emami.emmusic.security.repo.EmAuthorityRepository
import com.emami.emmusic.security.repo.EmUserRepository
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class InitSecurity(
    val emUserRepository: EmUserRepository,
    val emAuthorityRepository: EmAuthorityRepository,
    val passwordEncoder: PasswordEncoder
) {

    @PostConstruct
    fun initMySecurity() {
        println("******************************InitSecurity")
        if (emUserRepository.count() == 0L) {
            val systemAdministrator = EmAuthority("System Administrator", "ROLE_SYSTEM_ADMIN", null)
            emAuthorityRepository.save(systemAdministrator)
            val emUserAdmin =
                EmUser(
                    "admin",
                    passwordEncoder.encode("admin@emami"),
                    "admin@mail.com",
                    "09136729464",
                    mutableListOf(systemAdministrator)
                )
            emUserRepository.save(emUserAdmin)
        }
    }
}