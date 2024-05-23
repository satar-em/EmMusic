package com.emami.emmusic.sms

import org.springframework.stereotype.Service

@Service
class SmsServiceImp: SmsService {
    override fun sendSms(msg: String, phoneNumber: String) {
        println("***** send sms ($msg)=>$phoneNumber")
    }
}