package com.emami.emmusic.security.model

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
data class EmUser(
    @Column(unique = true) private var username: String,
    private var password: String,
    var email: String,
    var phone: String,
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "em_user_authority",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "em_authority_id")],
    )
    @JsonIgnore
    var emAuthorities: MutableList<EmAuthority>?,
) : UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 10

    @JsonIgnore
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return emAuthorities?.map {
            SimpleGrantedAuthority(it.code)
        }?.toMutableList() ?: mutableListOf()
    }

    @JsonGetter("authorities")
    fun getJsonAuthorities(): Any {
        return emAuthorities?.map {
            mutableMapOf("name" to it.name, "code" to it.code)
        }?.toMutableList() ?: mutableListOf<Any>()
    }

    @JsonIgnore
    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun toString(): String {
        return "EmUser(id=$id, username='$username')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EmUser

        if (username != other.username) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }


}