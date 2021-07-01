package ro.asis.guest.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.bson.types.ObjectId
import ro.asis.commons.model.Address
import ro.asis.commons.model.Cart

data class Guest(
    @JsonProperty("id")
    val id: String = ObjectId.get().toHexString(),

    @JsonProperty("firstName")
    val firstName: String,

    @JsonProperty("lastName")
    val lastName: String,

    @JsonProperty("phoneNumber")
    val phoneNumber: String,

    @JsonProperty("email")
    val email: String,

    @JsonProperty("address")
    val address: Address,

    @JsonProperty("cart")
    val cart: Cart
)
