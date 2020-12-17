package server

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.rmi.Remote
import java.rmi.RemoteException

@JsonSerialize
interface FruitMartRMI : Remote {
    @Throws(RemoteException::class)
    fun addItem(dbItem: Map<String, Any>): String

    @Throws(RemoteException::class)
    fun updatePrice(dbItem: Map<String, Any>): String

    @Throws(RemoteException::class)
    fun deleteItem(dbItem: Map<String, Any>): String

    @Throws(RemoteException::class)
    fun calcItemCost(dbItem: Map<String, Any>): String

}




