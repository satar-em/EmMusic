package com.emami.emmusic.web.api.pub

import com.emami.emmusic.db.model.EmNewRegister
import com.emami.emmusic.db.repo.EmNewRegisterRepository
import com.emami.emmusic.security.repo.EmUserRepository
import com.emami.emmusic.sms.SmsService
import com.emami.emmusic.web.pojo.ActiveNewRegisterRequest
import com.emami.emmusic.web.pojo.NewRegisterRequest
import com.emami.emmusic.web.pojo.NewRegisterResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.ThreadLocalRandom

@RestController
@RequestMapping(path = ["/api/public/register"], produces = ["application/json"])
class RegisterApi(val smsService: SmsService, val emNewRegisterRepository: EmNewRegisterRepository,val emUserRepository: EmUserRepository) {

    /*@Scheduled(cron = "0 0/3 * ? * *")
    fun onSchedule() {
        println("salam")
    }*/

    @PostMapping(path = ["/new"])
    fun newRegister(@RequestBody request: NewRegisterRequest): ResponseEntity<Any> {
        try {
            val smsCode = ThreadLocalRandom.current().nextInt(100000, 1000000).toString()
            val exception = Calendar.getInstance()
            exception.add(Calendar.MINUTE, 1)
            if (emUserRepository.findByPhoneNumber(request.phoneNumber).isPresent) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(mutableMapOf("message" to "phone number used before"))
            }
            val emNewRegister = emNewRegisterRepository.save(
                EmNewRegister(
                    smsCode,
                    request.username,
                    request.password,
                    request.phoneNumber,
                    request.gender,
                    exception
                )
            )
            val webCode = emNewRegister.id
            smsService.sendSms(smsCode, request.phoneNumber)
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
            return ResponseEntity.ok(mutableMapOf("message" to "Active New Register Successfully"))
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mutableMapOf("message" to "your sms code not valid try again"))
    }
}