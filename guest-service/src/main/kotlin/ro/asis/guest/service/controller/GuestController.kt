package ro.asis.guest.service.controller

import com.github.fge.jsonpatch.JsonPatch
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import ro.asis.commons.exceptions.ResourceNotFoundException
import ro.asis.commons.filters.GuestFilters
import ro.asis.guest.dto.Guest
import ro.asis.guest.service.model.entity.GuestEntity
import ro.asis.guest.service.model.mappers.GuestMapper
import ro.asis.guest.service.service.GuestService

@RestController
@RequestMapping("guests")
class GuestController(
    private val service: GuestService,
    private val guestMapper: GuestMapper
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(GuestEntity::class.java)
    }

    @GetMapping
    fun getAllGuests(filters: GuestFilters): List<Guest> = guestMapper.toApi(service.findAllGuests(filters))

    @GetMapping("{guestId}")
    fun getGuest(@PathVariable guestId: String): Guest = service.findGuest(guestId)
        .map { guestMapper.toApi(it) }
        .orElseThrow { ResourceNotFoundException("Could not find guest with id $guestId") }

    @PostMapping
    fun addGuest(@RequestBody guest: Guest): Guest =
        guestMapper.toApi(service.addGuest(guestMapper.toEntity(guest)))

    @PatchMapping("{guestId}")
    fun patchGuest(@PathVariable guestId: String, @RequestBody patch: JsonPatch): Guest =
        guestMapper.toApi(service.patchGuest(guestId, patch))

    @DeleteMapping("{guestId}")
    fun deleteGuest(@PathVariable guestId: String): Guest = service.deleteGuest(guestId)
        .map { guestMapper.toApi(it) }
        .orElseGet { null }
}
