package com.frankfiorante.cta_tracker.client.model

import com.fasterxml.jackson.annotation.JsonProperty

data class CtaArrival(
    @JsonProperty("stpId") val stopId: String,
    @JsonProperty("rt") val route: String,
    @JsonProperty("destNm") val destination: String,
    @JsonProperty("arrT") val arrivalTime: String,
    @JsonProperty("isApp") val isApproaching: String,
)
