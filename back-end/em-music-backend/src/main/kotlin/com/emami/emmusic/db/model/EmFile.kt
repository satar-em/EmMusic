package com.emami.emmusic.db.model

import jakarta.persistence.*
import org.springframework.util.MimeType

@Entity
data class EmFile(val name: String, @Column(unique = true) val path: String,val mimeType: String, val size: Long) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
    override fun toString(): String {
        return "EmFile(name='$name', id=$id)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EmFile

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
