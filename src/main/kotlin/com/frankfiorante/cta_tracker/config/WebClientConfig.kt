package com.frankfiorante.cta_tracker.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig(
    @Value($$"${cta.api.base-url}") private val ctaBaseUrl: String,
    @Value($$"${ntfy.topic-url}") private val ntfyTopicUrl: String
) {

    @Bean("ctaWebClient")
    fun ctaWebClient(): WebClient = WebClient.builder().baseUrl(ctaBaseUrl).build()

    @Bean("ntfyWebClient")
    fun ntfyWebClient(): WebClient = WebClient.builder().baseUrl(ntfyTopicUrl).build()
}
