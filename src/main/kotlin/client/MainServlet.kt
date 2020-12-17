package client

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dtos.CartItem
import dtos.Item
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
                    val item: Item = mapper.readValue(req.inputStream)
                    val itemMap = createDBItem(item, "add")
                    response = vendorOps!!.addItem(itemMap)
                }

                "/updatePrice" -> {
                    val item: Item = mapper.readValue(req.inputStream)
                    val itemMap = createDBItem(item, "update")
                    response = vendorOps!!.updatePrice(itemMap)

                }

                "/deleteItem" -> {
                    val item: Item = mapper.readValue(req.inputStream)
                    val itemMap = createDBItem(item, "delete")
                    response = vendorOps!!.deleteItem(itemMap)


                }

                "/calcItemCost" -> {
                    val cartItem: CartItem = mapper.readValue(req.inputStream)
                    val itemMap = validateCartItem(cartItem)
                    response = vendorOps!!.calcItemCost(itemMap)
                }
            }

        } catch (exception: Exception) {
            println(exception)
//            throw exception
        }
        return response
    }

    private fun createDBItem(item: Item, action: String): Map<String, Any> {
        // Data validation for add and update
        when (action) {
            "add" -> {
                val missing = null
                val validationError = "Validation error: Missing Parameter"
                when (missing) {
                    item.name -> error("$validationError name")
                    item.price -> error("$validationError price")
                    item.unit -> error("$validationError unit")
                }
            }
            "update" -> {
                val missing = null
                val validationError = "Validation error: Missing Parameter"
                when (missing) {
                    item.name -> error("$validationError name")
                    item.price -> error("$validationError price")
                }
            }
            "delete" -> {
                val missing = null
                val validationError = "Validation error: Missing Parameter"
                if (missing == item.name) error("$validationError name")
            }

        }
        return mapper.convertValue(item, object : TypeReference<Map<String, Any>>() {})

    }

    private fun validateCartItem(cartItem: CartItem): Map<String, Any> {
        val missing = null
        val validationError = "Validation error: Missing Parameter"
        when (missing) {
            cartItem.name -> error("$validationError name")
            cartItem.quantity -> error("$validationError quantity")
        }
        return mapper.convertValue(cartItem, object : TypeReference<Map<String, Any>>() {})

    }
}