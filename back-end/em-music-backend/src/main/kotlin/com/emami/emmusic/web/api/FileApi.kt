package com.emami.emmusic.web.api

import com.emami.emmusic.db.model.EmFile
import com.emami.emmusic.db.repo.EmFileRepository
import com.emami.emmusic.file.FileSystemStorageService
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.MimeType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files

@RestController
@RequestMapping(path = ["/api/file"], produces = ["application/json"])
class FileApi(val fileSystemStorageService: FileSystemStorageService, val emFileRepository: EmFileRepository) {

    @PostMapping(path = ["/upload"], consumes = ["multipart/form-data"])
    fun uploadFile(
        @RequestPart("file") file: MultipartFile,
        @RequestPart("name", required = false) name: String?
    ): ResponseEntity<Any> {
        val emfile: EmFile
        //val filename = file.originalFilename
        var filename = name
        if (name.isNullOrBlank()) {
            filename = file.originalFilename
        }
        try {
            emfile = emFileRepository.save(fileSystemStorageService.storeAudio(file, filename!!))
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mutableMapOf("error" to e.message))
        }
        return ResponseEntity.ok(mutableMapOf<String, Any>("file" to emfile))
    }

    @GetMapping(path = ["/download/{id}"])
    fun uploadFile(
        @PathVariable("id") id: String,
    ): ResponseEntity<Any> {
        val emFile = emFileRepository.findById(id)
        if (emFile.isEmpty) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
                .body(mutableMapOf("message" to "file with id=$id not found"))
        }
        val file = fileSystemStorageService.loadFile(emFile.get().path)
        if (file != null) {
            return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.asMediaType(MimeType.valueOf(emFile.get().mimeType)))
                //.contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length())
                .header("Content-Disposition", "attachment;filename=\"${file.name}\"")
                .body(FileSystemResource(file))
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
            .body(mutableMapOf("message" to "cannot load file with id=$id"))
    }

}