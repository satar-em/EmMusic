package com.emami.emmusic.sms

interface SmsService {
    fun sendSms(msg: String,phoneNumber: String)
}