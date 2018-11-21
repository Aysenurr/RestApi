package com.restapi.application.database

import com.restapi.application.image.Metadata
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.io.File

@Controller
@RequestMapping(path=["/progressive"])
class ProgressiveController {
    private val logger = LoggerFactory.getLogger(ProgressiveController::class.java)
    private val pathOfCurrentDirectory = System.getProperty("user.dir")
    private val pathToProgressiveImages = "$pathOfCurrentDirectory\\src\\main\\resources\\images\\progressive\\"

    @Autowired
    private var progressiveRepository: ProgressiveRepository? = null

    @EventListener(ApplicationReadyEvent::class)
    fun addAllProgressiveImagesOnStartupToDatabase() {
        progressiveRepository!!.deleteAll()
        logger.info("Load progressive images initially to database")
        File(pathToProgressiveImages).listFiles().forEach {
            if (it.extension == "jpeg" || it.extension == "jpg") {
                addNewImageToDatabase(file = it)
            }
        }
    }

    @GetMapping(path = ["/addNewImage"])
    @ResponseBody fun addNewImage(@RequestParam fileName: String) {
        try {
            val file = File(pathToProgressiveImages + fileName)
            addNewImageToDatabase(file)
        } catch (e: Exception) {
            logger.error("Cannot find or load file '$fileName'!\n${e.stackTrace}!")
        }
    }

    @GetMapping(path = ["/getImage"])
    fun getImage(@RequestParam fileName: String, model: Model): Model {
        val images = progressiveRepository!!.findAll()
        images.forEach {
            if (it.getName() == fileName) {
                model["title"] = it.getName()!!
                model["image"] = it.getPath()!!
            }
        }
        logger.info("Return progressive image '$fileName' to browser")
        return model
    }

    private fun addNewImageToDatabase(file: File) {
        val progressive = Progressive()
        val metaData = Metadata(file)

        progressive.setName(file.name)
        progressive.setExtension(file.extension)
        progressive.setHeight(metaData.getHeight())
        progressive.setWidth(metaData.getWidth())
        progressive.setSize(file.readBytes().size)
        progressive.setPath(file.path)

        progressiveRepository!!.save(progressive)
        logger.info("Progressive image '${file.name}' saved in database")
    }
}