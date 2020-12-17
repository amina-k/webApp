package server

import java.rmi.registry.LocateRegistry
import java.rmi.server.UnicastRemoteObject

class FruitMartServer : FruitMartServiceImpl() {
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {

            val fruitMart = FruitMartServiceImpl()

            val stub: FruitMartRMI = UnicastRemoteObject.exportObject(fruitMart, 0) as FruitMartRMI

            val registry = LocateRegistry.createRegistry(1099)

            registry.bind("FruitMart", stub)
            System.err.println("Server ready")

        }
    }
}