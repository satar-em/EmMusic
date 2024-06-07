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
            val systemAdministrator = EmAuthority("System Administrator", "ROLE_ROOT_ADMIN", null)
            emAuthorityRepository.save(systemAdministrator)
            val publicUserRole = EmAuthority("System Public User", "ROLE_USER", null)
            emAuthorityRepository.save(publicUserRole)
            val emUserAdmin =
                EmUser(
                    "admin",
                    passwordEncoder.encode("admin@emami"),
                    "admin",
                    "admin",
                    "none",
                    "none",
                    "admin@mail.com",
                    "09136729464",
                    mutableListOf(systemAdministrator,publicUserRole)
                )
            emUserRepository.save(emUserAdmin)

            val emUserSystem =
                EmUser(
                    "system-administrator",
                    passwordEncoder.encode("system@system"),
                    "system",
                    "system",
                    "none",
                    "none",
                    "system@system.com",
                    "00000000000",
                    mutableListOf(systemAdministrator,publicUserRole)
                )
            emUserRepository.save(emUserSystem)
        }
    }
}