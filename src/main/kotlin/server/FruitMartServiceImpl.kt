package server


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dtos.FetchedItem
import org.json.JSONObject
import khttp.delete as httpDelete
import khttp.get as httpGet
import khttp.post as httpPost
import khttp.put as httpPut

const val baseRDBUrl = "https://fruitmart-ad2a6-default-rtdb.europe-west1.firebasedatabase.app/"
val mapper = ObjectMapper()

open class FruitMartServiceImpl : FruitMartRMI {

    override fun addItem(dbItem: Map<String, Any>): String {

        println("Adding item and price to DB")
        val response = httpPost(
            "${baseRDBUrl}items.json",
            json = JSONObject(dbItem)
        )
        return (response.toString())
    }

    override fun updatePrice(dbItem: Map<String, Any>): String {

        println("Updating item price on DB")
        val itemDetails = httpGet("""${baseRDBUrl}items.json?&orderBy="name"&equalTo="${dbItem["name"]}"""")

        val fetchedItem = FetchedItem(indexedItem = mapper.readValue(itemDetails.content))

        val response = httpPut(
            "${baseRDBUrl}items/${fetchedItem.indexedItem.keys.first()}/price.json",
            data = dbItem["price"]
        )
        return (response.toString())
    }

    override fun deleteItem(dbItem: Map<String, Any>): String {

        println("Deleting item from DB")
        val itemDetails = httpGet("""${baseRDBUrl}items.json?&orderBy="name"&equalTo="${dbItem["name"]}"""")
        val fetchedItem = FetchedItem(indexedItem = mapper.readValue(itemDetails.content))

        val response = httpDelete(
            "${baseRDBUrl}items/${fetchedItem.indexedItem.keys.first()}.json"
        )
        return response.toString()
    }

    override fun calcItemCost(dbItem: Map<String, Any>): String {

        println("Deleting item from DB")
        val itemDetails = httpGet("""${baseRDBUrl}items.json?&orderBy="name"&equalTo="${dbItem["name"]}"""")
        val fetchedItem = FetchedItem(indexedItem = mapper.readValue(itemDetails.content))
        val itemSpecifics = fetchedItem.indexedItem.values.first()!!

        val response = JSONObject(
            mapOf(
                "name" to itemSpecifics.name!!,
                "pricePerUnit" to itemSpecifics.price.toString(),
                "totalCost" to (itemSpecifics.price!! * dbItem["quantity"] as Int).toString()
            )
        )
        return response.toString()
    }
}




