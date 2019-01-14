package com.restapi.application.welcome

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/**
 * This controller handles incoming http requests for the welcome page
 *
 * @author      Markus Graf
 * @see         org.springframework.stereotype.Controller
 * @see         org.springframework.web.bind.annotation.GetMapping
 * @version     1.0
 */

@Controller
class WelcomeController {

    /**
     * Returns the html file as response to the browser when requesting the url "localhost:8080/"
     * and "127.0.0.1:8080/"
     *
     * @return  html file name for the welcome page
     */
    
    @GetMapping("/")
    fun showWelcomePage(): String = "index"
}