package com.emami.emmusic

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EmMusicBackendApplication

fun main(args: Array<String>) {
    runApplication<EmMusicBackendApplication>(*args)
}
