import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dtos.CartItem
import dtos.FetchedItem
import dtos.Item
import khttp.responses.Response
import org.json.JSONObject
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import khttp.get as httpGet
import khttp.put as httpPut
import khttp.post as httpPost
import khttp.delete as httpDelete

const val baseRDBUrl = "https://fruitmart-ad2a6-default-rtdb.europe-west1.firebasedatabase.app/"

@WebServlet(name = "FruitMart", value = ["/"])
class HomeController : HttpServlet() {


    override fun doPost(req: HttpServletRequest, res: HttpServletResponse) {
        return res.writer.write(processRequest(req).toString())
    }

    private fun processRequest(req: HttpServletRequest): String? {
        val path = req.servletPath
        var response: String? = null
        val mapper = ObjectMapper()

        try {
        when (path) {
            "/addPrice" -> {
                val item: Item = mapper.readValue(req.inputStream)

                response = httpPost(
                    "${baseRDBUrl}items.json",
                    json = JSONObject(item)
                ).toString()
            }

            "/updatePrice" -> {
                val item: Item = mapper.readValue(req.inputStream)
                val dbItem = httpGet("""${baseRDBUrl}items.json?&orderBy="name"&equalTo="${item.name}"""")
                val fetchedItem = FetchedItem(indexedItem  = mapper.readValue(dbItem.content))

                response = httpPut(
                    "${baseRDBUrl}items/${fetchedItem.indexedItem.keys.first()}/price.json",
                    data = item.price
                ).toString()
            }

            "/deleteItem" -> {
                val item: Item = mapper.readValue(req.inputStream)
                val dbItem = httpGet("""${baseRDBUrl}items.json?&orderBy="name"&equalTo="${item.name}"""")
                val fetchedItem = FetchedItem(indexedItem  = mapper.readValue(dbItem.content))

                response = httpDelete(
                    "${baseRDBUrl}items/${fetchedItem.indexedItem.keys.first()}.json",
                    data = item.price
                ).toString()
            }

            "/calItemCost" -> {
                val cartItem: CartItem = mapper.readValue(req.inputStream)
                val dbItem = httpGet("""${baseRDBUrl}items.json?&orderBy="name"&equalTo="${cartItem.name}"""")
                val fetchedItem = FetchedItem(indexedItem  = mapper.readValue(dbItem.content))
                val itemDetails = fetchedItem.indexedItem.values.first()!!

                response = JSONObject(mapOf(
                    "name" to itemDetails.name!!,
                    "pricePerUnit" to itemDetails.price.toString(),
                    "totalCost" to (itemDetails.price!! * cartItem.quantity!!).toString()))
                    .toString()
            }
        }

        } catch (exception: Exception) {
            println(exception)
            throw exception
        }
        return response
    }
}