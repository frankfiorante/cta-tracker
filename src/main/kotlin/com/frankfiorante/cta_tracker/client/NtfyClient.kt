package com.frankfiorante.cta_tracker.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientException

@Service
class NtfyClient(
    @Qualifier("ntfyWebClient") private val webClient: WebClient,
) {

    private val log = LoggerFactory.getLogger(NtfyClient::class.java)

    fun sendNotification(message: String) {
        try {
            webClient.post()
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(message)
                .retrieve()
                .toBodilessEntity()
                .block()
            log.debug("Notification sent: {}", message)
        } catch (e: WebClientException) {
            log.warn("Failed to send ntfy notification: {}", e.message)
        }
    }
}
