package com.frankfiorante.cta_tracker.service

import com.frankfiorante.cta_tracker.client.CtaApiClient
import com.frankfiorante.cta_tracker.client.NtfyClient
import com.frankfiorante.cta_tracker.repository.StationRepository
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ArrivalNotificationService(
    private val stationRepository: StationRepository,
    private val ctaApiClient: CtaApiClient,
    private val ntfyClient: NtfyClient,
    @Value($$"${cta.proximity-meters}") private val proximityMeters: Double,
    @Value($$"${cta.arrival-window-minutes}") private val arrivalWindowMinutes: Long,
) {

    private val log = LoggerFactory.getLogger(ArrivalNotificationService::class.java)
    private val chicago = ZoneId.of("America/Chicago")
    private val arrivalDateTimeFormat = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    fun processLocation(lat: Double, lon: Double) {
        val nearbyStations = stationRepository.findNearby(lat, lon, proximityMeters)
        if (nearbyStations.isEmpty()) {
            log.debug("No stations within {} meters of ({}, {})", proximityMeters, lat, lon)
            return
        }

        val now = Instant.now()
        val nowChicago = LocalDateTime.ofInstant(now, chicago)
        val windowEnd = nowChicago.plusMinutes(arrivalWindowMinutes)

        for (station in nearbyStations) {
            val arrivals = ctaApiClient.getArrivals(station.mapId)
            val upcoming = arrivals.filter {
                if (station.stopIds.isNotEmpty() && it.stopId !in station.stopIds) return@filter false
                if (it.isApproaching == "1") return@filter true
                try {
                    val arrTime = LocalDateTime.parse(it.arrivalTime, arrivalDateTimeFormat)
                    arrTime.isAfter(nowChicago) && arrTime.isBefore(windowEnd)
                } catch (e: Exception) {
                    log.warn("Could not parse arrivalTime '{}': {}", it.arrivalTime, e.message)
                    false
                }
            }

            if (upcoming.isEmpty()) {
                log.debug("No arrivals within {} minutes for station {}", arrivalWindowMinutes, station.name)
                continue
            }

            val lines = upcoming.joinToString("\n") {
                val eta = when {
                    it.isDelayed == "1" -> "Delayed"
                    it.isApproaching == "1" -> "Now"
                    else -> {
                        val arrivalDateTime = LocalDateTime.parse(it.arrivalTime, arrivalDateTimeFormat)
                        "${Duration.between(nowChicago, arrivalDateTime).toMinutes()} min"
                    }
                }
                "${it.route} → ${it.destination}: $eta"
            }
            val message = "${station.name}\n$lines"

            ntfyClient.sendNotification(message)
        }
    }
}
