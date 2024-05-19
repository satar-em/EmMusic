package com.emami.emmusic.security.api

import com.emami.emmusic.security.model.LoginRequest
import com.emami.emmusic.security.model.LoginResponse
import com.emami.emmusic.security.repo.EmUserRepository
import com.emami.emmusic.security.service.JwtService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/login"], produces = ["application/json"])
class Login(
    val jwtService: JwtService,
    val authenticationManager: AuthenticationManager,
    val emUserRepository: EmUserRepository
) {
    @PostMapping
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginRequest.username,
                    loginRequest.password
                )
            )
            val user = emUserRepository.findByUsername(loginRequest.username)
            if (user.isPresent) {
                val jwtToken = jwtService.generateToken(user.get())
                val loginResponse = LoginResponse(0, "Login successfully", mutableMapOf("token" to jwtToken))
                return ResponseEntity.status(HttpStatus.OK).body(loginResponse)
            }
        } catch (e: Exception) {
            val loginResponse = LoginResponse(-1, "cannot login , username of password not correct")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResponse)
        }
        val loginResponse = LoginResponse(-1, "cannot login , unknown error")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResponse)
    }
}