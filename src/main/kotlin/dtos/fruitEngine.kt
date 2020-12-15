package dtos

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize
data class Item(
    val name: String? = "",
    val price: Int? = 0
)

@JsonDeserialize
data class FetchedItem(
    val indexedItem: Map<String?, Item?> = mapOf()
)

@JsonDeserialize
data class CartItem(
    val name: String? = "",
    val quantity: Int? = 0
)
