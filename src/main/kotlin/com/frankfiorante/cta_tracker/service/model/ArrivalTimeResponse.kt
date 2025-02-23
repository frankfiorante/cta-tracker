package com.frankfiorante.cta_tracker.service.model

data class ArrivalTimeResponse(
    val ctatt: Ctatt
)

data class Ctatt(
    val tmst: String, // Timestamp of the response
    val errCd: String, // Error code
    val errNm: String?, // Error name (nullable)
    val eta: List<Eta> // List of arrival predictions
)

data class Eta(
    val staId: String, // Station ID
    val stpId: String, // Stop ID
    val staNm: String, // Station name
    val stpDe: String, // Stop description
    val rn: String, // Run number
    val rt: String, // Route (e.g., "Blue")
    val destSt: String, // Destination station ID
    val destNm: String, // Destination name
    val trDr: String, // Train direction
    val prdt: String, // Prediction time
    val arrT: String, // Arrival time
    val isApp: String, // Is approaching?
    val isSch: String, // Is scheduled?
    val isDly: String, // Is delayed?
    val isFlt: String, // Is faulted?
    val flags: String?, // Flags (nullable)
    val lat: String?, // Latitude
    val lon: String?, // Longitude
    val heading: String? // Heading
)
