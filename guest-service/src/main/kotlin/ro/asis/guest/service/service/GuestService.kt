package ro.asis.guest.service.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ro.asis.commons.exceptions.ResourceNotFoundException
import ro.asis.commons.filters.GuestFilters
import ro.asis.guest.service.model.entity.GuestEntity
import ro.asis.guest.service.repository.GuestDao
import ro.asis.guest.service.repository.GuestRepository
import ro.asis.guest.service.service.validator.GuestValidator
import java.util.*

@Service
class GuestService(
    private val dao: GuestDao,
    private val mapper: ObjectMapper,
    private val validator: GuestValidator,
    private val repository: GuestRepository,
    private val notificationsService: GuestNotificationsService
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(GuestEntity::class.java)
    }

    fun findAllGuests(filters: GuestFilters): List<GuestEntity> = dao.findGuests(filters)

    fun findGuest(guestId: String): Optional<GuestEntity> = repository.findById(guestId)

    fun addGuest(guest: GuestEntity): GuestEntity {
        validator.validateNewOrThrow(guest)
        val dbGuest = repository.save(guest)
        notificationsService.notifyGuestCreated(dbGuest)
        return dbGuest
    }

    fun deleteGuest(guestId: String): Optional<GuestEntity> {
        validator.validateExistsOrThrow(guestId)
        val guestToDelete = repository.findById(guestId)
        guestToDelete.ifPresent { deleteExistingGuest(it) }
        return guestToDelete
    }

    fun patchGuest(guestId: String, patch: JsonPatch): GuestEntity {
        validator.validateExistsOrThrow(guestId
        )
        val dbGuest = getOrThrow(guestId)
        val patchedGuestJson = patch.apply(mapper.valueToTree(dbGuest))
        val patchedGuest = mapper.treeToValue(patchedGuestJson, GuestEntity::class.java)

        validator.validateReplaceOrThrow(guestId, patchedGuest)
        copyGuest(patchedGuest, dbGuest)
        notificationsService.notifyGuestEdited(dbGuest)

        return repository.save(dbGuest)
    }

    private fun copyGuest(newGuest: GuestEntity, dbGuest: GuestEntity) {
        LOG.info("Copying guest: $newGuest")
        dbGuest.address = newGuest.address
        dbGuest.cart = newGuest.cart
        dbGuest.email = newGuest.email
        dbGuest.phoneNumber = newGuest.phoneNumber
        dbGuest.firstName = newGuest.firstName
        dbGuest.lastName = newGuest.lastName
    }

    private fun deleteExistingGuest(guest: GuestEntity) {
        LOG.info("Deleting guest: $guest")
        notificationsService.notifyGuestDeleted(guest)
        repository.delete(guest)
    }

    private fun getOrThrow(guestId: String): GuestEntity = repository
        .findById(guestId)
        .orElseThrow { ResourceNotFoundException("Could not find guest with id $guestId") }
}


