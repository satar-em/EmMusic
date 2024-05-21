package com.emami.emmusic.web.api

import com.emami.emmusic.db.model.EmPlaylist
import com.emami.emmusic.db.repo.EmPlayListRepository
import com.emami.emmusic.security.service.MySecurityService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api/playlist"], produces = ["application/json"])
class PlayListApi(val emPlayListRepository: EmPlayListRepository, val mySecurityService: MySecurityService) {

    @PostMapping("/create", consumes = ["application/json"])
    fun cretePlayList(@RequestBody emPlaylist: EmPlaylist): ResponseEntity<Any> {
        return try {
            emPlaylist.owner = mySecurityService.getCurrentUserLogged()
            ResponseEntity.ok(emPlayListRepository.save(emPlaylist))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mutableMapOf("message" to e.message))
        }
    }

    @PutMapping("/update/{id}", consumes = ["application/json"])
    fun updatePlayList(@PathVariable("id") id: String, @RequestBody emPlaylist: EmPlaylist): ResponseEntity<Any> {
        return try {
            val playListInDB = emPlayListRepository.findById(id.toLong())
            if (playListInDB.isPresent) {
                playListInDB.get().name = emPlaylist.name
                playListInDB.get().musics = emPlaylist.musics
                ResponseEntity.ok(emPlayListRepository.save(playListInDB.get()))
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(mutableMapOf("message" to "play list with id=$id not found"))
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mutableMapOf("message" to e.message))
        }
    }

    @GetMapping("/get-all")
    fun getAllPlayList(
        @RequestParam(name = "page-size", required = false, defaultValue = "20") pageSize: String,
        @RequestParam(name = "page-number", required = false, defaultValue = "0") pageNumber: String,
        @RequestParam(name = "sort-by", required = false, defaultValue = "id") sortBy: String,
    ): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(
                emPlayListRepository.findAll(
                    PageRequest.of(pageNumber.toInt(), pageSize.toInt(), Sort.by(Sort.Direction.ASC, sortBy))
                ).toList()
            )
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mutableMapOf("message" to e.message))
        }
    }

    @GetMapping("/get/{id}")
    fun getPlayListById(@PathVariable("id") id: String): ResponseEntity<Any> {
        return try {
            val playlist = emPlayListRepository.findById(id.toLong())
            if (playlist.isPresent) {
                ResponseEntity.ok(playlist.get())
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(mutableMapOf("message" to "play list with id=$id not found"))
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mutableMapOf("message" to e.message))
        }
    }

    @DeleteMapping("/delete-all")
    fun deleteAllPlayList(): ResponseEntity<Any> {
        return try {
            emPlayListRepository.deleteAll()
            ResponseEntity.ok(mutableMapOf("message" to "delete all successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mutableMapOf("message" to e.message))
        }
    }

    @DeleteMapping("/delete/{id}")
    fun deletePlayListById(@PathVariable("id") id: String): ResponseEntity<Any> {
        return try {
            val playlist = emPlayListRepository.findById(id.toLong())
            if (playlist.isEmpty) {
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(mutableMapOf("message" to "playlist with id=$id not found"))
            } else {
                emPlayListRepository.delete(playlist.get())
                ResponseEntity.ok(mutableMapOf("message" to "delete playlist with id=$id successfully"))
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mutableMapOf("message" to e.message))
        }
    }
}