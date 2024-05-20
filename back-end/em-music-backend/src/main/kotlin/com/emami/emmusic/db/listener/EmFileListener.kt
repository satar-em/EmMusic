package com.emami.emmusic.db.listener

import com.emami.emmusic.db.model.EmFile
import jakarta.persistence.PrePersist
import java.util.Calendar


class EmFileListener {
    @PrePersist
    fun prePersist(emFile: EmFile) {
        emFile.uploadedAt = Calendar.getInstance()
    }

}