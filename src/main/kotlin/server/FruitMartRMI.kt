package server

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import dtos.CartItem
import dtos.FetchedItem
import dtos.Item
import dtos.Orders
import java.rmi.Remote
import java.rmi.RemoteException

@JsonSerialize
interface FruitMartRMI : Remote {
    @Throws(RemoteException::class)
    fun fetchItem(name: String): FetchedItem?

    @Throws(RemoteException::class)
    fun fetchAllItems(): String

    @Throws(RemoteException::class)
    fun addItem(dbItem: Item): String

    @Throws(RemoteException::class)
    fun updatePrice(dbItem: Item): String

    @Throws(RemoteException::class)
    fun deleteItem(dbItem: Item): String

    @Throws(RemoteException::class)
    fun calcItemCost(dbItem: CartItem): String

    @Throws(RemoteException::class)
    fun fetchAllOrders(): String

    @Throws(RemoteException::class)
    fun addOrder(dbOrder: Orders): String
}




