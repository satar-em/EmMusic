package com.emami.emmusic.file

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "em-music.storage")
class StorageProperties(){
     lateinit var basePath: String
}
