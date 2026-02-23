package com.frankfiorante.cta_tracker.client

import com.frankfiorante.cta_tracker.client.model.CtaArrival
import com.frankfiorante.cta_tracker.client.model.CtaResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientException
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class CtaApiClient(
    @Qualifier("ctaWebClient") private val webClient: WebClient,
    @Value($$"${cta.api.key}") private val apiKey: String,
) {

    private val log = LoggerFactory.getLogger(CtaApiClient::class.java)

    fun getArrivals(mapId: Int): List<CtaArrival> {
        return try {
            val response = webClient.get()
                .uri {
                    it.path("/ttarrivals.aspx")
                        .queryParam("key", apiKey)
                        .queryParam("mapid", mapId)
                        .queryParam("outputType", "JSON")
                        .build()
                }
                .retrieve()
                .bodyToMono<CtaResponse>()
                .block()
            response?.ctaTrainTrackerResponse?.eta ?: emptyList()
        } catch (e: WebClientException) {
            log.warn("CTA API request failed for mapId={}: {}", mapId, e.message)
            emptyList()
        }
    }
}