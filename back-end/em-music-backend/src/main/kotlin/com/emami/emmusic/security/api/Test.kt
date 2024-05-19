package com.emami.emmusic.security.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/private/test"], produces = ["application/json"])
class Test {
    @GetMapping(path = ["/hello"])
    fun login(): ResponseEntity<Any> {
        val response = mutableMapOf("message" to "hello")
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }
}