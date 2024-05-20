package com.emami.emmusic.db.repo

import com.emami.emmusic.db.model.EmFile
import com.emami.emmusic.security.model.EmUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface EmFileRepository : JpaRepository<EmFile, String> {
    @Query("select e from EmFile e where e.uploadedBy = :user")
    fun findFileUploadedByUser(@Param("user") user: EmUser): List<EmFile>
}