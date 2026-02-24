package com.frankfiorante.cta_tracker.repository

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import org.springframework.stereotype.Repository

@Repository
class StationRepository {

    private val stations = listOf(
        Station(40230, "Cumberland", 41.984246, -87.838028),
        Station(40750, "Harlem (O'Hare Branch)", 41.98227, -87.8089),
        Station(40380, "Clark/Lake (Blue Line)", 41.885737, -87.630886, stopIds = setOf("30374", "30375")),
        Station(40370, "Washington (Blue Line)", 41.883164, -87.62944),
        Station(40790, "Monroe (Blue Line)", 41.880703, -87.629378),
    )

    fun findNearby(lat: Double, lon: Double, radiusMeters: Double): List<Station> =
        stations.filter { haversineMeters(lat, lon, it.lat, it.lon) <= radiusMeters }

    private fun haversineMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6_371_000.0
        val phi1 = Math.toRadians(lat1)
        val phi2 = Math.toRadians(lat2)
        val dPhi = Math.toRadians(lat2 - lat1)
        val dLambda = Math.toRadians(lon2 - lon1)
        val a = sin(dPhi / 2).pow(2) + cos(phi1) * cos(phi2) * sin(dLambda / 2).pow(2)
        return r * 2 * atan2(sqrt(a), sqrt(1 - a))
    }
}

data class Station(
    val mapId: Int,
    val name: String,
    val lat: Double,
    val lon: Double,
    val stopIds: Set<String> = emptySet(),
)