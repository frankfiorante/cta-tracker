package com.frankfiorante.cta_tracker.service.model

enum class StationMapping(val stopId: String, val parentStopId: String) {
    HARLEM_OHARE_NORTH(stopId = "30145", parentStopId = "40750"),
    HARLEM_OHARE_SOUTH(stopId = "30146", parentStopId = "40750"),
}
