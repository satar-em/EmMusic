package com.emami.emmusic.init

import com.emami.emmusic.db.repo.EmNewRegisterRepository
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class InitRegister(
    val emNewRegisterRepository: EmNewRegisterRepository
) {

    @PostConstruct
    fun initMySecurity() {
        println("******************************InitRegister")
        emNewRegisterRepository.findByExpireTimeBefore(Calendar.getInstance()).forEach {
            println("delete NewRegister with id=${it.id}")
            emNewRegisterRepository.delete(it)
        }
    }
}