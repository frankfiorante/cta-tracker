package com.frankfiorante.cta_tracker.client.model

import com.fasterxml.jackson.annotation.JsonProperty

data class CtaResponse(
    @JsonProperty("ctatt") val ctaTrainTrackerResponse: CtaTrainTrackerResponse,
)

data class CtaTrainTrackerResponse(
    @JsonProperty("tmst") val timestamp: String,
    @JsonProperty("errCd") val errorCode: String,
    @JsonProperty("errNm") val errorName: String?,
    @JsonProperty("eta") val eta: List<CtaArrival>,
)
