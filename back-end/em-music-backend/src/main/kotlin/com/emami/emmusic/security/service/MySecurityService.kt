package com.emami.emmusic.security.service

import com.emami.emmusic.security.model.EmUser
import com.emami.emmusic.security.repo.EmUserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.*

@Service
class MySecurityService(val emUserRepository: EmUserRepository) {

    fun getCurrentUserLogged(): EmUser {
        if (
            SecurityContextHolder.getContext().authentication != null &&
            SecurityContextHolder.getContext().authentication.isAuthenticated &&
            SecurityContextHolder.getContext().authentication.principal != null &&
            SecurityContextHolder.getContext().authentication.principal is Optional<*> &&
            (SecurityContextHolder.getContext().authentication.principal as Optional<*>).isPresent &&
            ((SecurityContextHolder.getContext().authentication.principal as Optional<*>).get() is EmUser)
        ) {
            return ((SecurityContextHolder.getContext().authentication.principal as Optional<*>).get() as EmUser)
        }
        return emUserRepository.findByUsername("system-administrator").get()
    }
}