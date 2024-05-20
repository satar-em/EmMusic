package com.emami.emmusic.db.listener

import com.emami.emmusic.db.model.EmPlaylist
import jakarta.persistence.PrePersist
import java.util.Calendar


class EmPlayListListener {
    @PrePersist
    fun prePersist(emPlayList: EmPlaylist) {
        emPlayList.createdAt = Calendar.getInstance()
    }

}