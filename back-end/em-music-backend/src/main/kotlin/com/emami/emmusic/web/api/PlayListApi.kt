package com.emami.emmusic.web.api

import com.emami.emmusic.db.model.EmPlaylist
import com.emami.emmusic.db.repo.EmPlayListRepository
import com.emami.emmusic.security.service.MySecurityService
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

    @GetMapping("/get-all")
    fun getAllPlayList(): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(emPlayListRepository.findAll())
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mutableMapOf("message" to e.message))
        }
    }
}