package com.emami.emmusic.db.repo

import com.emami.emmusic.db.model.EmFile
import org.springframework.data.jpa.repository.JpaRepository

interface EmFileRepository :JpaRepository<EmFile,Long>{
}