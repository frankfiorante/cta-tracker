package com.frankfiorante.cta_tracker.controller

import com.frankfiorante.cta_tracker.service.CtaService
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class CtaController(private val service: CtaService) {

    @ResponseStatus(NO_CONTENT)
    @GetMapping("/track")
    fun getHarlemTime() {
        service.getHarlemOhareNorth()
    }
}
