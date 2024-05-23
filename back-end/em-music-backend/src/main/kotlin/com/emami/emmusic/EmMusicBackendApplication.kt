package com.emami.emmusic

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
/*@EnableScheduling*/
class EmMusicBackendApplication

fun main(args: Array<String>) {
    runApplication<EmMusicBackendApplication>(*args)
}
