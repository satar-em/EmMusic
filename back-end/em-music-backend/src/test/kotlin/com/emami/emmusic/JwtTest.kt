package com.emami.emmusic

import com.emami.emmusic.security.model.EmUser
import com.emami.emmusic.security.service.JwtService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootTest
class JwtTest {
    @Autowired
    private lateinit var jwtService: JwtService

    @Test
    fun testCreate(){
        val token = jwtService.generateToken(EmUser("admin", BCryptPasswordEncoder().encode("admin@123"), "","09131254521", null))
        println()
    }
    @Test
    fun testParse(){
        val username = jwtService.extractUsername("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxNDc2MDk4OCwiZXhwIjoxNzE0NzY0NTg4fQ.qQwp8dm7UbmIc_i6ylq6NhLgCqmG1U3M_Dao9XacZDo")
        val isValid = jwtService.isTokenValid("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxNDc2MDk4OCwiZXhwIjoxNzE0NzY0NTg4fQ.qQwp8dm7UbmIc_i6ylq6NhLgCqmG1U3M_Dao9XacZDo",EmUser("admin", BCryptPasswordEncoder().encode("admin@123"), "","09131254521", null))
        println()
    }
}