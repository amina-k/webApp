package client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dtos.CartItem
import dtos.FetchedItem
import dtos.Item
import org.json.JSONObject
import server.FruitMartRMI
import java.net.MalformedURLException
import java.rmi.Naming
import java.rmi.NotBoundException
import java.rmi.RemoteException
import java.rmi.registry.LocateRegistry
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import khttp.delete as httpDelete
import khttp.get as httpGet
import khttp.post as httpPost
import khttp.put as httpPut

const val baseRDBUrl = "https://fruitmart-ad2a6-default-rtdb.europe-west1.firebasedatabase.app/"
val mapper = ObjectMapper()


@WebServlet(name = "FruitMart", value = ["/"])
class HomeController : HttpServlet() {
    private var vendorOps: FruitMartRMI? = null


    @Throws(MalformedURLException::class, RemoteException::class, NotBoundException::class)
    override fun doPost(req: HttpServletRequest, res: HttpServletResponse) {
        LocateRegistry.getRegistry(1099)
        vendorOps = Naming.lookup("FruitMart") as FruitMartRMI

        return res.writer.write(processRequest(req).toString())
    }

    private fun processRequest(req: HttpServletRequest): String? {
        val path = req.servletPath
        var response: String? = null

        try {
            when (path) {
                "/addPrice" -> {
                    mapper.readValue(req.inputStream) as Item// To ensure correct parameters are passed
                    val itemMap: Map<String, String> =
                        mapper.readValue(req.inputStream) // To ensure correct parameters are passed
                    response = vendorOps!!.addPrice(itemMap)
                }

                "/updatePrice" -> {
                    val item: Item = mapper.readValue(req.inputStream)
                    val dbItem = httpGet("""${baseRDBUrl}items.json?&orderBy="name"&equalTo="${item.name}"""")
                    val fetchedItem = FetchedItem(indexedItem = mapper.readValue(dbItem.content))

                    response = httpPut(
                        "${baseRDBUrl}items/${fetchedItem.indexedItem.keys.first()}/price.json",
                        data = item.price
                    ).toString()
                }

                "/deleteItem" -> {
                    val item: Item = mapper.readValue(req.inputStream)
                    val dbItem = httpGet("""${baseRDBUrl}items.json?&orderBy="name"&equalTo="${item.name}"""")
                    val fetchedItem = FetchedItem(indexedItem = mapper.readValue(dbItem.content))

                    response = httpDelete(
                        "${baseRDBUrl}items/${fetchedItem.indexedItem.keys.first()}.json",
                        data = item.price
                    ).toString()
                }

                "/calItemCost" -> {
                    val cartItem: CartItem = mapper.readValue(req.inputStream)
                    val dbItem = httpGet("""${baseRDBUrl}items.json?&orderBy="name"&equalTo="${cartItem.name}"""")
                    val fetchedItem = FetchedItem(indexedItem = mapper.readValue(dbItem.content))
                    val itemDetails = fetchedItem.indexedItem.values.first()!!

                    response = JSONObject(
                        mapOf(
                            "name" to itemDetails.name!!,
                            "pricePerUnit" to itemDetails.price.toString(),
                            "totalCost" to (itemDetails.price!! * cartItem.quantity!!).toString()
                        )
                    )
                        .toString()
                }
            }

        } catch (exception: Exception) {
            println(exception)
//            throw exception
        }
        return response
    }
}