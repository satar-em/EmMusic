package com.emami.emmusic.config

import com.emami.emmusic.file.StorageProperties
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MyAppRunner{
    companion object {
        val inits = mutableListOf<() -> Unit>()
    }

    @Bean
    fun getMyAppRunner(storageProperties: StorageProperties): ApplicationRunner {
        return ApplicationRunner {
            println("******************************ApplicationRunner")
            for (init in inits) {
                init()
            }
        }
    }

}