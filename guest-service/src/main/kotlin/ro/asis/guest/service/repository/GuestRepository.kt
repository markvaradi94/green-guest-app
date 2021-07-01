package ro.asis.guest.service.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ro.asis.guest.service.model.entity.GuestEntity

interface GuestRepository : MongoRepository<GuestEntity, String> {
    fun existsByEmail(email: String): Boolean
}
