package com.emami.emmusic.config

import com.emami.emmusic.db.repo.EmFileRepository
import com.emami.emmusic.security.repo.EmAuthorityRepository
import org.springframework.stereotype.Component

@Component
class MyStaticConfigs(
    emFileRepository: EmFileRepository,
    emAuthorityRepository: EmAuthorityRepository,

) {
    init {
        staticEmFileRepository = emFileRepository
        staticEmAuthorityRepository = emAuthorityRepository
    }

    companion object {
        lateinit var staticEmFileRepository: EmFileRepository
        lateinit var staticEmAuthorityRepository: EmAuthorityRepository
    }
}