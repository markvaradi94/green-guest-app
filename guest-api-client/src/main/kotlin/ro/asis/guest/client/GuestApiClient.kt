package ro.asis.guest.client

import com.github.fge.jsonpatch.JsonPatch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity.EMPTY
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpMethod.GET
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import org.springframework.web.client.patchForObject
import org.springframework.web.client.postForObject
import org.springframework.web.util.UriComponentsBuilder
import ro.asis.commons.filters.GuestFilters
import ro.asis.guest.dto.Guest
import java.util.*
import java.util.Optional.ofNullable

@Component
class GuestApiClient(
    @Value("\${guest-service-location:NOT_DEFINED}")
    private val baseUrl: String,
    private val restTemplate: RestTemplate
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(Guest::class.java)
    }

    fun getAllGuests(filters: GuestFilters): List<Guest> {
        val url = buildQueriedUrl(filters)

        return restTemplate.exchange(
            url,
            GET,
            EMPTY,
            object : ParameterizedTypeReference<List<Guest>>() {}
        ).body ?: listOf()
    }

    fun getGuest(guestId: String): Optional<Guest> {
        val url = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/guests/$guestId")
            .toUriString()

        return ofNullable(restTemplate.getForObject(url, Guest::class))
    }

    fun addGuest(guest: Guest): Guest {
        val url = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/guests")
            .toUriString()

        return restTemplate.postForObject(url, guest, Guest::class)
    }

    fun patchGuest(guestId: String, patch: JsonPatch): Optional<Guest> {
        val url = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/guests/$guestId")
            .toUriString()

        return ofNullable(restTemplate.patchForObject(url, patch))
    }

    fun deleteGuest(guestId: String): Optional<Guest> {
        val url = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/guests/$guestId")
            .toUriString()

        return ofNullable(
            restTemplate.exchange(
                url,
                DELETE,
                EMPTY,
                Guest::class.java
            ).body
        )
    }

    private fun buildQueriedUrl(filters: GuestFilters): String {
        val builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/guests/")

        ofNullable(filters.email)
            .ifPresent { builder.queryParam("email", it) }
        ofNullable(filters.phoneNumber)
            .ifPresent { builder.queryParam("phoneNumber", it) }
        ofNullable(filters.city)
            .ifPresent { builder.queryParam("city", it) }
        ofNullable(filters.streetName)
            .ifPresent { builder.queryParam("streetName", it) }
        ofNullable(filters.firstName)
            .ifPresent { builder.queryParam("firstName", it) }
        ofNullable(filters.lastName)
            .ifPresent { builder.queryParam("lastName", it) }

        return builder.toUriString()
    }
}
