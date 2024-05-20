package com.emami.emmusic.db.model

import com.emami.emmusic.db.listener.EmFileListener
import com.emami.emmusic.security.model.EmUser
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.util.*

@Entity
@EntityListeners(EmFileListener::class)
data class EmFile(
    val name: String,
    @Column(unique = true) val path: String,
    val mimeType: String,
    val size: Long,
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    val uploadedBy: EmUser,
) {
    @Column(columnDefinition = "timestamp with time zone")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "utc")
    var uploadedAt: Calendar = Calendar.getInstance()

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String = ""
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

    @JsonGetter("uploadedBy")
    fun getJsonUploadedBy(): MutableMap<String, Any> {
        return mutableMapOf("id" to uploadedBy.id, "username" to uploadedBy.username)
    }


}
