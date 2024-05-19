package com.emami.emmusic

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
class UserTest {

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
    fun createUser() {
        val user = EmUser("admin", BCryptPasswordEncoder().encode("admin@123"), "","09131254521", null)
        emUserRepository.save(user)
        readAll()
    }

    @Test
    @Rollback(false)
    fun setUserRole() {
        val users=emUserRepository.findAll()
        val authorities=emAuthorityRepository.findAll()
        users[0].emAuthorities=authorities
        emUserRepository.save(users[0])
        readAll()
    }

    @Test
    @Rollback(false)
    fun clearUserRole() {
        val users=emUserRepository.findAll()
        users[0].emAuthorities=null
        readAll()
    }


}