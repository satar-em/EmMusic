package com.emami.emmusic.web.api

import com.emami.emmusic.db.repo.EmFileRepository
import com.emami.emmusic.security.service.MySecurityService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/user"], produces = ["application/json"])
class UserApi(val mySecurityService: MySecurityService,val emFileRepository: EmFileRepository) {
    @GetMapping("/current")
    fun getCurrentUserInfo(): ResponseEntity<Any> {
        val currentUser = mySecurityService.getCurrentUserLogged()
        if (currentUser.id != 2L) {
            return ResponseEntity.ok(currentUser)
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mutableMapOf("message" to "User not found"))
    }
}