package com.restapi.application.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import java.io.BufferedInputStream
import com.drew.imaging.ImageMetadataReader
import java.net.URL
import java.io.IOException
import com.drew.imaging.ImageProcessingException
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.attribute.BasicFileAttributes

class DatabaseHelper {
    private val logger: Logger = LoggerFactory.getLogger(DatabaseHelper::class.java)

    object Progressive : Table() {
        val id = integer("id").autoIncrement().primaryKey()
        var name = varchar("name", length = 50)
        val path = varchar("path", length = 50)
        val size = varchar("size", length = 50)
        val width = integer("width")
        val height = integer("height")
        val comments = varchar("comments", length = 50)
    }

    object Baseline : Table() {
        val id = integer("id").autoIncrement().primaryKey()
        val name = varchar("name", length = 50)
        val path = varchar("path", length = 50)
        val size = varchar("size", length = 50)
        val width = integer("width")
        val height = integer("height")
        val comments = varchar("comments", length = 50)
    }

    inner class Metadata {
        var size: String = ""
        var width: String = ""
        var height: String = ""
        var comments: String = ""
    }

    fun loadTables() {
        try {
            Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
            logger.info("Database loaded")

            transaction {
                create(Baseline, Progressive)
            }
            logger.info("Tables created")
        } catch (e: Exception) {
            logger.error("Error during loading databases! {}", e.message)
        }
    }

    fun dropTables() {
        try {
            transaction {
                drop(Baseline, Progressive)
            }
            logger.info("Database dropped")
        } catch (e: Exception) {
            logger.error("Error during dropping datatables! {}", e.message)
        }
    }

    private fun getMetadata(path: String): Metadata {
        val metadataObject = Metadata()

        try {
            val stream = BufferedInputStream(URL(path).openStream())
            val metadata = ImageMetadataReader.readMetadata(stream, -1)
            val attributes = Files.readAttributes(
                    File(path).toPath(),
                    BasicFileAttributes::class.java,
                    LinkOption.NOFOLLOW_LINKS
            )
            metadataObject.size = attributes.size().toString()

            for (directory in metadata.directories) {
                println("Loading directories ...")
                println(directory.name)

                for (tag in directory.tags) {
                    println("Loading tags ...")
                    println(tag.description)
                    println(tag.tagName)
                    println(tag.directoryName)
                    println(tag.tagTypeHex)

                    when (tag.tagName.toString()) {
                        "ImageWidth" -> metadataObject.width = tag.description
                        "ImageHeight" -> metadataObject.height = tag.description
                        "ImageDescription" -> metadataObject.comments = tag.description
                        else -> logger.info("Unused tag ${tag.tagName} found.")
                    }
                }
            }
        } catch (e: ImageProcessingException) {
            logger.error("Metadata cannot be loaded! {}", e.message)
            return Metadata()
        } catch (e: IOException) {
            logger.error("Metadata cannot be loaded! {}", e.message)
            return Metadata()
        }
        return metadataObject
    }

    fun insertProgressive(_name: String, _path: String) {
        //val metadata = getMetadata(_path)

        transaction {
            Progressive.insert  {
                it[name] = "test"
                it[size] = "test"
                it[path] = "test"
                it[comments] = "test"
                it[width] = 5
                it[height] = 5
            }
            for (city in Progressive.selectAll()) {
                println("abcd: ${city[Progressive.size]}")
            }
        }
    }

    fun insertBaseline(
            file: BufferedInputStream,
            name: String,
            path: String
    ) {
        val metadata = getMetadata(path)

        transaction {

        }
    }

    fun getProgressive(name: String) {

    }

    fun getBaselinename(name: String) {

    }

    fun deleteProgressive(name: String) {

    }

    fun deleteBaseline(name: String) {

    }
}
