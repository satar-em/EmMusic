package com.emami.emmusic.file

import com.emami.emmusic.db.model.EmFile
import com.emami.emmusic.security.service.MySecurityService
import org.apache.tika.Tika
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@Service
class FileSystemStorageService(storageProperties: StorageProperties, val mySecurityService: MySecurityService) {
    private val rootBasePath: Path = Paths.get(storageProperties.basePath)

    fun storeAudio(file: MultipartFile, fileName: String): EmFile {
        try {
            var fileNameNew = fileName
            if (file.isEmpty) {
                throw Exception("Failed to store empty file.")
            }
            val byteArrayOutputStream = ByteArrayOutputStream()
            file.inputStream.use { inputStream ->
                inputStream.transferTo(byteArrayOutputStream)
            }
            val firstClone: InputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
            val mimeType = Tika().detect(firstClone)
            if (!mimeType.startsWith("audio/")) {
                throw Exception("this is not a audio file.")
            }
            var destinationFile = rootBasePath.resolve(Paths.get(fileNameNew)).normalize().toAbsolutePath()
            while (Files.exists(destinationFile)) {
                fileNameNew =
                    "${destinationFile.toFile().nameWithoutExtension}-copy.${destinationFile.toFile().extension}"
                destinationFile = rootBasePath.resolve(Paths.get(fileNameNew)).normalize().toAbsolutePath()
            }
            if (destinationFile.parent != rootBasePath.toAbsolutePath()) {
                throw Exception(
                    "Cannot store file outside current directory."
                )
            }
            ByteArrayInputStream(byteArrayOutputStream.toByteArray()).use { inputStream ->
                Files.copy(inputStream, destinationFile)
            }
            return EmFile(
                rootBasePath.resolve(Paths.get(fileName)).normalize().toAbsolutePath().toFile().nameWithoutExtension,
                fileNameNew,
                mimeType,
                file.size,
                mySecurityService.getCurrentUserLogged()
            )
        } catch (e: Exception) {
            throw Exception("Failed to store file. (${e.message})")
        }
    }

    fun loadFile(path: String): File? {
        val destinationFile = rootBasePath.resolve(Paths.get(path)).normalize().toAbsolutePath()
        if (Files.exists(destinationFile)) {
            return destinationFile.toFile()
        }
        return null
    }
}