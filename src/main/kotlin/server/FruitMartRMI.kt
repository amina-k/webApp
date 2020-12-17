package server

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.rmi.Remote
import java.rmi.RemoteException

@JsonSerialize
interface FruitMartRMI : Remote {
    @Throws(RemoteException::class)
    fun addPrice(request: Map<String, String>): String
}




