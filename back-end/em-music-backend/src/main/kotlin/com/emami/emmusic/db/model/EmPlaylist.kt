package com.emami.emmusic.db.model

import com.emami.emmusic.config.MyStaticConfigs
import com.emami.emmusic.db.listener.EmPlayListListener
import com.emami.emmusic.security.model.EmUser
import com.fasterxml.jackson.annotation.*
import jakarta.persistence.*
import java.util.*

@Entity
@EntityListeners(EmPlayListListener::class)
data class EmPlaylist(
    var name: String,
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "em_playList_emFile",
        joinColumns = [JoinColumn(name = "playlist_id")],
        inverseJoinColumns = [JoinColumn(name = "file_id")]
    )
    @JsonIgnore
    var musics: MutableList<EmFile> = mutableListOf(),
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var owner: EmUser? = null,
) {

    @Column(columnDefinition = "timestamp with time zone")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "utc")
    var createdAt: Calendar = Calendar.getInstance()

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
    override fun toString(): String {
        return "EmPlayList(name='$name', id=$id)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EmPlaylist

        if (name != other.name) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }

    @JsonGetter("owner")
    fun getJsonOwner(): MutableMap<String, Any>? {
        if (owner != null) return mutableMapOf("id" to owner!!.id, "username" to owner!!.username)
        return null
    }

    @JsonGetter("musics")
    fun getJsonMusics(): MutableList<Any> {
        return musics.map {
            mutableMapOf("id" to it.id, "name" to it.name)
        }.toMutableList()
    }


    @JsonSetter("musics", contentNulls = Nulls.SKIP)
    fun setJsonMusics(musicsJson: MutableList<Any>) {
        musics.clear()
        musicsJson.forEach {
            if (it is Map<*, *> && it.containsKey("id")) {
                if (MyStaticConfigs.staticEmFileRepository.findById(it["id"].toString()).isEmpty) {
                    throw EntityNotFoundException("not found file with id=${it["id"].toString()}")
                }
                musics.add(MyStaticConfigs.staticEmFileRepository.findById(it["id"].toString()).get())
            }
        }
    }
}