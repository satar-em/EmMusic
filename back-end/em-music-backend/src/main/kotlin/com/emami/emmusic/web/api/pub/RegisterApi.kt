package com.emami.emmusic.web.api.pub

import com.emami.emmusic.db.model.EmNewRegister
import com.emami.emmusic.db.repo.EmNewRegisterRepository
import com.emami.emmusic.security.model.EmUser
import com.emami.emmusic.security.repo.EmAuthorityRepository
import com.emami.emmusic.security.repo.EmUserRepository
import com.emami.emmusic.sms.SmsService
import com.emami.emmusic.web.pojo.ActiveNewRegisterRequest
import com.emami.emmusic.web.pojo.NewRegisterRequest
import com.emami.emmusic.web.pojo.NewRegisterResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.concurrent.ThreadLocalRandom


@RestController
@RequestMapping(path = ["/api/public/register"], produces = ["application/json"])
@EnableAsync
@EnableScheduling
class RegisterApi(
    val smsService: SmsService,
    val emNewRegisterRepository: EmNewRegisterRepository,
    val emUserRepository: EmUserRepository,
    val emAuthorityRepository: EmAuthorityRepository,
    val passwordEncoder: PasswordEncoder,
    val taskScheduler: TaskScheduler
) {

    /*@Scheduled(cron = "0 0/3 * ? * *")
    fun onSchedule() {
        println("salam")
    }*/

    @PostMapping(path = ["/new"])
    fun newRegister(@Valid @RequestBody request: NewRegisterRequest): ResponseEntity<Any> {
        try {
            val smsCode = ThreadLocalRandom.current().nextInt(100000, 1000000).toString()
            val exception = Calendar.getInstance()
            exception.add(Calendar.MINUTE, 1)
            if (emUserRepository.findByPhoneNumber(request.phoneNumber).isPresent) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(mutableMapOf("message" to "phone number used before"))
            }
            if (emUserRepository.findByUsername(request.username).isPresent) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(mutableMapOf("message" to "username used before"))
            }
            if (emUserRepository.findByEmail(request.email).isPresent) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(mutableMapOf("message" to "email used before"))
            }
            val emNewRegister = emNewRegisterRepository.save(
                EmNewRegister(
                    smsCode,
                    request.firstname,
                    request.lastname,
                    request.username,
                    request.password,
                    request.phoneNumber,
                    request.email,
                    request.address,
                    request.gender,
                    exception
                )
            )
            val webCode = emNewRegister.id
            smsService.sendSms(smsCode, request.phoneNumber)
            taskScheduler.schedule(
                {
                    println("deleting emNewRegister with id=${emNewRegister.id}")
                    try {
                        emNewRegisterRepository.delete(emNewRegister)
                    }catch (ex: Exception){
                        println("error while deleting emNewRegister with id=${emNewRegister.id}: ${ex.message}")
                    }
                },
                Instant.now().plusSeconds(60)
            )
            return ResponseEntity.ok(NewRegisterResponse(webCode))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(mutableMapOf("message" to "error : ${e.message}"))
        }
    }

    @PostMapping(path = ["/new/active"])
    fun activeNewRegister(@RequestBody request: ActiveNewRegisterRequest): ResponseEntity<Any> {
        val emNewRegister = emNewRegisterRepository.findById(request.webCode)
        if (emNewRegister.isEmpty) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mutableMapOf("message" to "error , maybe your validation is expired"))
        }
        if (emNewRegister.get().expireTime.before(Calendar.getInstance())) {
            emNewRegisterRepository.delete(emNewRegister.get())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mutableMapOf("message" to "your validation is expired"))
        }
        if (emNewRegister.get().smsCode == request.smsCode) {
            //MyStaticConfigs.staticEmAuthorityRepository.findByName("System Public User").get()
            val emUser = emUserRepository.save(
                EmUser(
                    emNewRegister.get().username,
                    passwordEncoder.encode(emNewRegister.get().password),
                    emNewRegister.get().firstname,
                    emNewRegister.get().lastname,
                    emNewRegister.get().address,
                    emNewRegister.get().gender,
                    emNewRegister.get().email,
                    emNewRegister.get().phoneNumber,
                    mutableListOf(emAuthorityRepository.findByName("System Public User").get())
                )
            )
            emNewRegisterRepository.delete(emNewRegister.get())
            return ResponseEntity.ok(mutableMapOf("message" to "Active New Register Successfully", "user" to emUser))
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mutableMapOf("message" to "your sms code not valid try again"))
    }
}