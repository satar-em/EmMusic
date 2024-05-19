package com.emami.emmusic

import com.emami.emmusic.security.model.EmAuthority
import com.emami.emmusic.security.model.EmUser
import com.emami.emmusic.security.repo.EmAuthorityRepository
import com.emami.emmusic.security.repo.EmUserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.annotation.Rollback

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorityTest {

    @Autowired
    lateinit var emUserRepository: EmUserRepository

    @Autowired
    lateinit var emAuthorityRepository: EmAuthorityRepository

    @Test
    fun readAll() {
        val allEmUsers = emUserRepository.findAll()
        val allEmAuthorities = emAuthorityRepository.findAll()
        println()
    }

    @Test
    @Rollback(false)
    fun createAuthority() {
        val authority = EmAuthority("emami", "ROLE_ADMIN", null)
        emAuthorityRepository.save(authority)
        readAll()
    }

    @Test
    @Rollback(false)
    fun setAuthorityUser() {
        val allEmUsers = emUserRepository.findAll()
        val allEmAuthorities = emAuthorityRepository.findAll()
        allEmAuthorities[0].users = allEmUsers
        readAll()
    }
    @Test
    @Rollback(false)
    fun clearAuthorityUser() {
        val allEmAuthorities = emAuthorityRepository.findAll()
        allEmAuthorities[0].users = null
        readAll()
    }


}