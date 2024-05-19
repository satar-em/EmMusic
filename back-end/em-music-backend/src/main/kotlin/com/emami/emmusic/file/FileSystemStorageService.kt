package com.emami.emmusic.file

import com.emami.emmusic.db.model.EmFile
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@Service
class FileSystemStorageService(storageProperties: StorageProperties) {
    private val rootBasePath: Path = Paths.get(storageProperties.basePath)

    fun store(file: MultipartFile, fileName: String): EmFile {
        try {
            var fileNameNew = fileName
            if (file.isEmpty) {
                throw Exception("Failed to store empty file.")
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
            file.inputStream.use { inputStream ->
                Files.copy(inputStream, destinationFile)
            }
            return EmFile(fileName, fileNameNew,Files.probeContentType(destinationFile), file.size)
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