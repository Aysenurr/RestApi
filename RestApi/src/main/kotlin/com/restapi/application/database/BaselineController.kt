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
@RequestMapping(path=["/basement"])
class BaselineController {
    private val logger = LoggerFactory.getLogger(BaselineController::class.java)
    private val pathOfCurrentDirectory = System.getProperty("user.dir")
    private val pathToBaselineImages = "$pathOfCurrentDirectory\\src\\main\\resources\\images\\baseline\\"

    @Autowired
    private var baselineRepository: BaselineRepository? = null

    @EventListener(ApplicationReadyEvent::class)
    fun addAllBaselineImagesOnStartupToDatabase() {
        baselineRepository!!.deleteAll()
        logger.info("Load baseline images initially to database")
        File(pathToBaselineImages).listFiles().forEach {
            if (it.extension == "jpeg" || it.extension == "jpg") {
                addNewImageToDatabase(file = it)
            }
        }
    }

    @GetMapping(path = ["/addNewImage"])
    @ResponseBody fun addNewImage(@RequestParam fileName: String) {
        try {
            val file = File(pathToBaselineImages + fileName)
            addNewImageToDatabase(file)
        } catch (e: Exception) {
            logger.error("Cannot find or load file '$fileName'!\n${e.stackTrace}")
        }
    }

    @GetMapping(path = ["/getImage"])
    fun getImage(@RequestParam fileName: String, model: Model): Model {
        val images = baselineRepository!!.findAll()
        images.forEach {
            if (it.getName() == fileName) {
                model["title"] = it.getName()!!
                model["image"] = it.getPath()!!
            }
        }
        logger.info("Return baseline image '$fileName' to browser")
        return model
    }

    private fun addNewImageToDatabase(file: File) {
        val baseline = Baseline()
        val metaData = Metadata(file)

        baseline.setName(file.name)
        baseline.setExtension(file.extension)
        baseline.setHeight(metaData.getHeight())
        baseline.setWidth(metaData.getWidth())
        baseline.setSize(file.readBytes().size)
        baseline.setPath(file.path)

        baselineRepository!!.save(baseline)
        logger.info("Baseline image '${file.name}' saved in database")
    }
}