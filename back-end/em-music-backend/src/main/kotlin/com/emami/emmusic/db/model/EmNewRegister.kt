package com.emami.emmusic.db.model

import jakarta.persistence.*
import java.util.Calendar

@Entity
data class EmNewRegister(
    val smsCode: String,
    val username: String,
    val password: String,
    @Column(unique = true) val phoneNumber: String,
    val gender: String,
    val expireTime: Calendar
) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)

    var id: String = ""
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EmNewRegister

        if (username != other.username) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }

    override fun toString(): String {
        return "EmNewRegister(username='$username', id='$id')"
    }


}
