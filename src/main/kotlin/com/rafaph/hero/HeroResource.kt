package com.rafaph.hero

import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/hero")
@Produces(MediaType.TEXT_PLAIN)
@RequestScoped
class HeroResource @Inject constructor(
    private val command: HeroCommand,
    private val eventSubscriber: EventSubscriber,
) {
    private var response: Response? = null

    @GET
    suspend fun handle(): Response {
        eventSubscriber.on(GetHelloWorldEvent::class.java, this::onSuccess)

        command.execute()

        return this.response ?: Response.status(500).entity("error").build()
    }

    private fun onSuccess(event: GetHelloWorldEvent) {
        response = Response.status(200).entity(event.message).build()
    }
}
