package com.emami.emmusic.config

import com.emami.emmusic.db.repo.EmFileRepository
import org.springframework.stereotype.Component

@Component
class MyStaticConfigs(emFileRepository: EmFileRepository) {
    init {
        staticEmFileRepository = emFileRepository
    }

    companion object {
        lateinit var staticEmFileRepository: EmFileRepository
    }
}