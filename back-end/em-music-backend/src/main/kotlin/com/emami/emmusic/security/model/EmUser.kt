package com.emami.emmusic.security.model

import com.emami.emmusic.db.model.EmFile
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.Calendar

@Entity
data class EmUser(
    @Column(unique = true) private var username: String,
    private var password: String,
    var email: String,
    @Column(unique = true) var phoneNumber: String,
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

    @Column(columnDefinition = "timestamp with time zone")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "utc")
    var createdAt: Calendar = Calendar.getInstance()

    @OneToMany(mappedBy = "uploadedBy", fetch = FetchType.EAGER)
    @JsonIgnore
    val fileUploaded = mutableListOf<EmFile>()

    @JsonGetter("fileUploaded")
    fun getJsonFileUploaded(): MutableList<Any> {
        return fileUploaded.map {
            mutableMapOf("id" to it.id, "name" to it.name, "mimeType" to it.mimeType)
        }.toMutableList()
    }

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
