package com.emami.emmusic.security.model

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
data class EmAuthority(
    @Column(unique = true) var name: String,
    var code: String,
    @ManyToMany
    @JoinTable(
        name = "em_user_authority",
        joinColumns = [JoinColumn(name = "em_authority_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")],
    )
    @JsonIgnore
    var users: MutableList<EmUser>?,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @JsonGetter("users")
    fun getJsonUsers(): Any {
        return users?.map {
            mutableMapOf("id" to it.id, "username" to it.username)
        }?.toMutableList() ?: mutableListOf<Any>()
    }

    override fun toString(): String {
        return "EmAuthority(name='$name', id=$id)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EmAuthority

        if (name != other.name) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }


}
