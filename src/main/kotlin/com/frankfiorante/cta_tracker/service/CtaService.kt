package com.frankfiorante.cta_tracker.service

import com.frankfiorante.cta_tracker.service.model.ArrivalTimeResponse
import com.frankfiorante.cta_tracker.service.model.StationMapping.HARLEM_OHARE_NORTH
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.Disposable
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class CtaService(
    @Value($$"${transit-chicago.url}") private val baseUrl: String,
    @Value($$"${transit-chicago.key}") private val key: String,
) {
    private val webClient = WebClient.create()

    fun getHarlemOhareNorth(): Disposable {
        return webClient.get()
            .uri(baseUrl) {
                it.queryParam("key", key)
//                    .queryParam("mapid", HARLEM_OHARE_NORTH.parentStopId)
                    .queryParam("stpid", HARLEM_OHARE_NORTH.stopId)
                    .queryParam("outputType", "JSON")
                    .build()
            }
            .retrieve()
            .bodyToMono(ArrivalTimeResponse::class.java)
            .map { response ->
                val currentTime = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

                response.ctatt.eta.forEach {
                    val arrivalTime = LocalDateTime.parse(it.arrT, formatter)
                    val minutesUntilArrival = Duration.between(currentTime, arrivalTime).toMinutes()
                    println("Train arriving at station ${it.staId} in $minutesUntilArrival minutes")
                }
            }
            .subscribe()
    }
}
