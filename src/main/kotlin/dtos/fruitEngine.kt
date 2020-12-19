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
