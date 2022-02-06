package com.rafaph.hero

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

data class GetHelloWorldEvent(val message: String)

@ApplicationScoped
class HeroCommand @Inject constructor(
    private val eventPublisher: EventPublisher,
) {
    suspend fun execute() {
        eventPublisher.publish(GetHelloWorldEvent("Hello world"))
    }
}
