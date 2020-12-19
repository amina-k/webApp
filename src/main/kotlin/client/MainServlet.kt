package client

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dtos.CartItem
import dtos.Item
import getErrorResponse
import getSuccessResponse
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
class MainServlet : HttpServlet() {
    private var vendorOps: FruitMartRMI? = null

    @Throws(MalformedURLException::class, RemoteException::class, NotBoundException::class)
    override fun doPost(req: HttpServletRequest, res: HttpServletResponse) {
        LocateRegistry.getRegistry(1099)
        vendorOps = Naming.lookup("FruitMart") as FruitMartRMI

        return res.writer.write(processRequest(req).toString())
    }

    @Throws(MalformedURLException::class, RemoteException::class, NotBoundException::class)
    override fun doGet(req: HttpServletRequest, res: HttpServletResponse) {
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
                    validateItem(item, "add")
                    response = vendorOps!!.addItem(item)
                }

                "/updatePrice" -> {
                    val item: Item = mapper.readValue(req.inputStream)
                    validateItem(item, "update")
                    response = vendorOps!!.updatePrice(item)

                }

                "/deleteItem" -> {
                    val item: Item = mapper.readValue(req.inputStream)
                    validateItem(item, "delete")
                    response = vendorOps!!.deleteItem(item)


                }

                "/calcItemCost" -> {
                    val cartItem: CartItem = mapper.readValue(req.inputStream)
                    validateCartItem(cartItem)
                    response = vendorOps!!.calcItemCost(cartItem)
                }

                "/fetchItem" -> {
                    val name: String = req.getParameter("name")
                    val fetchedItemResponse = vendorOps!!.fetchItem(name)
                    response = if (fetchedItemResponse != null) {
                        getSuccessResponse(
                            mapper.convertValue(
                                fetchedItemResponse.indexedItem,
                                object : TypeReference<Map<String, Any>>() {})
                        ).toString()
                    } else {
                        getErrorResponse().toString()
                    }
                }

                "/fetchAllItems" -> {
                    response = vendorOps!!.fetchAllItems()
                }


            }

        } catch (exception: Exception) {
            println(exception)
            throw exception
        }
        return response
    }

    private fun validateItem(item: Item, action: String) {
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
            "delete", "fetch" -> {
                val missing = null
                val validationError = "Validation error: Missing Parameter"
                if (missing == item.name) error("$validationError name")
            }

        }

    }

    private fun validateCartItem(cartItem: CartItem) {
        val missing = null
        val validationError = "Validation error: Missing Parameter"
        when (missing) {
            cartItem.name -> error("$validationError name")
            cartItem.quantity -> error("$validationError quantity")
        }
    }
}