package ro.asis.guest.service.repository

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import ro.asis.commons.filters.GuestFilters
import ro.asis.guest.service.model.entity.GuestEntity
import java.util.Optional.ofNullable

@Repository
class GuestDao(private val mongo: MongoTemplate) {
    fun findGuests(filters: GuestFilters): List<GuestEntity> {
        val query = Query()
        val criteria = buildCriteria(filters)

        if (criteria.isNotEmpty()) query.addCriteria(Criteria().andOperator(*criteria.toTypedArray()))

        return mongo.find(query, GuestEntity::class.java).toList()
    }

    private fun buildCriteria(filters: GuestFilters): List<Criteria> {
        val criteria = mutableListOf<Criteria>()

        ofNullable(filters.firstName)
            .ifPresent { criteria.add(Criteria.where("firstName").regex(".*$it.*", "i")) }
        ofNullable(filters.lastName)
            .ifPresent { criteria.add(Criteria.where("lastName").regex(".*$it.*", "i")) }
        ofNullable(filters.city)
            .ifPresent { criteria.add(Criteria.where("address.city").regex(".*$it.*", "i")) }
        ofNullable(filters.streetName)
            .ifPresent { criteria.add(Criteria.where("address.streetName").regex(".*$it.*", "i")) }
        ofNullable(filters.email)
            .ifPresent { criteria.add(Criteria.where("email").regex(".*$it.*", "i")) }
        ofNullable(filters.phoneNumber)
            .ifPresent { criteria.add(Criteria.where("phoneNumber").regex(".*$it.*")) }

        return criteria
    }
}
