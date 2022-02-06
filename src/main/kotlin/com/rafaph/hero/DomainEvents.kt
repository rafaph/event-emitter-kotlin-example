package com.rafaph.hero

import javax.inject.Singleton

typealias Handler<T> = (T) -> Unit

interface EventPublisher {
    fun <T : Any> publish(value: T)
}

interface EventSubscriber {
    fun <T : Any> on(type: Class<T>, handler: Handler<T>)
}

@Singleton
class DomainEvents : EventSubscriber, EventPublisher {
    private val listeners = mutableMapOf<Class<*>, List<Handler<Any>>>()

    override fun <T : Any> on(type: Class<T>, handler: Handler<T>) {
        @Suppress("UNCHECKED_CAST")
        val anyHandler = handler as Handler<Any>

        listeners[type] = listeners.getOrDefault(type, emptyList()) + listOf(anyHandler)
    }

    override fun <T : Any> publish(value: T) {
        listeners[value::class.java]?.let { handlers ->
            handlers.parallelStream().forEach { handler ->
                handler(value)
            }
        }
        listeners.clear()
    }
}
