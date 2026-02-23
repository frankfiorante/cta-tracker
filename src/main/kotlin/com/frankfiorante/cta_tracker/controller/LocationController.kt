package com.frankfiorante.cta_tracker.controller

import com.frankfiorante.cta_tracker.service.ArrivalNotificationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@RestController
class LocationController(
    private val arrivalNotificationService: ArrivalNotificationService,
) {

    @PostMapping("/location")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun receiveLocation(@RequestBody request: LocationRequest): Mono<Void> =
        Mono.fromRunnable<Void> { arrivalNotificationService.processLocation(request.lat, request.lon) }
            .subscribeOn(Schedulers.boundedElastic())
}

data class LocationRequest(
    val lat: Double,
    val lon: Double,
)
