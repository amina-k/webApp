package server


import khttp.post
import org.json.JSONObject

const val baseRDBUrl = "https://fruitmart-ad2a6-default-rtdb.europe-west1.firebasedatabase.app/"

open class FruitMartServiceImpl : FruitMartRMI {

    override fun addPrice(dbItem: Map<String, String>): String {

        println("Adding item and price to DB")
        val response = post(
            "${baseRDBUrl}items.json",
            json = JSONObject(dbItem)
        )
        return (response.toString())
    }
}




