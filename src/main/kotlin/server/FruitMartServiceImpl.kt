package server

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dtos.CartItem
import dtos.FetchedItem
import dtos.Item
import dtos.Orders
import getErrorResponse
import getSuccessResponse
import org.json.JSONObject
import khttp.delete as httpDelete
import khttp.get as httpGet
import khttp.post as httpPost
import khttp.put as httpPut

const val baseRTDBUrl = "https://fruitmart-ad2a6-default-rtdb.europe-west1.firebasedatabase.app/"

private val mapper = ObjectMapper()

open class FruitMartServiceImpl : FruitMartRMI {

    override fun fetchItem(name: String): FetchedItem? {

        println("Fetching one item from the DB")

        val response = httpGet("""${baseRTDBUrl}items.json?&orderBy="name"&equalTo="$name"""")
        val fetchedItem = FetchedItem(indexedItem = mapper.readValue(response.content))

        return if (response.statusCode == 200) fetchedItem else null
    }

    override fun fetchAllItems(): String {

        println("Fetching all items from DB")

        val response = httpGet(
            "${baseRTDBUrl}items.json"
        )

        return if (response.statusCode == 200) getSuccessResponse(mapper.readValue(response.content)).toString() else getErrorResponse().toString()
    }

    override fun addItem(dbItem: Item): String {

        println("Adding item and price to DB")

        val response = httpPost(
            "${baseRTDBUrl}items.json",
            json = JSONObject(dbItem)
        )
        return if (response.statusCode == 200) getSuccessResponse().toString() else getErrorResponse().toString()
    }

    override fun updatePrice(dbItem: Item): String {

        println("Updating item price/unit on DB")
        val fetchedItem = fetchItem(dbItem.name!!)!!

        val response = httpPut(
            "${baseRTDBUrl}items/${fetchedItem.indexedItem.keys.first()}.json",
            data = JSONObject(
                Item(
                    name = dbItem.name,
                    price = dbItem.price,
                    lastModified = dbItem.lastModified,
                    unit = dbItem.unit ?: fetchedItem.indexedItem.values.first()!!.unit.toString()
                )
            )
        )

        return if (response.statusCode == 200) getSuccessResponse().toString() else getErrorResponse().toString()
    }

    override fun deleteItem(dbItem: Item): String {

        println("Deleting item from DB")
        val fetchedItem = fetchItem(dbItem.name!!)!!

        val response = httpDelete(
            "${baseRTDBUrl}items/${fetchedItem.indexedItem.keys.first()}.json"
        )
        return if (response.statusCode == 200) getSuccessResponse().toString() else getErrorResponse().toString()
    }

    override fun calcItemCost(dbItem: CartItem): String {

        println("Calculating single item cost")
        val fetchedItem = fetchItem(dbItem.name!!)!!
        val itemSpecifics = fetchedItem.indexedItem.values.first()!!

        val response = getSuccessResponse(
            mapOf(
                "name" to itemSpecifics.name!!,
                "pricePerUnit" to itemSpecifics.price.toString(),
                "totalCost" to (itemSpecifics.price!! * dbItem.quantity as Int).toString()
            )
        )
        return response.toString()
    }

    override fun fetchAllOrders(): String {

        println("Fetching all orders from DB")

        val response = httpGet(
            "${baseRTDBUrl}orders.json"
        )

        return if (response.statusCode == 200) getSuccessResponse(mapper.readValue(response.content)).toString() else getErrorResponse().toString()
    }

    override fun addOrder(dbOrder: Orders): String {

        println("Adding order details to DB")

        val response = httpPost(
            "${baseRTDBUrl}orders.json",
            json = JSONObject(dbOrder)
        )
        return if (response.statusCode == 200) getSuccessResponse().toString() else getErrorResponse().toString()
    }


}




