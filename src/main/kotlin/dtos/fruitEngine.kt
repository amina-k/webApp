package dtos

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.io.Serializable
import java.time.Instant

@JsonDeserialize
data class Item(
    val name: String? = null,
    val price: Int? = null,
    val unit: String? = null,
    val lastModified: String? = Instant.now().toString()
) : Serializable

@JsonDeserialize
data class FetchedItem(
    val indexedItem: Map<String?, Item?> = mapOf()
) : Serializable

@JsonDeserialize
data class CartItem(
    val name: String? = null,
    val quantity: Int? = null
) : Serializable

@JsonDeserialize
data class Orders(
    val cashierName: String? = "",
    val orderDate: String? = "",
    val orderItems: List<OrderItem>? = null,
    val orderTotal: Double? = 0.0,
    val amountPaid: Double? = 0.0
) : Serializable

@JsonDeserialize
data class OrderItem(
    val item: Item? = null,
    var quantity: Double? = 0.0,
    var total: Double? = 0.0
) : Serializable