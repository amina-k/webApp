
# FruitMart
FruitMart is a fruit vendor application with 4 components
  - server (RMI implementation)
  - client (servlet calling server tasks)
  - android application
  - cloud database (firebase realtime DB)
 
 It is a kotlin set up for the applications (server, client, android app)

## Running the applications
The client runs of smart tomcat on IntelliJIdea

The server is also run using the built in run capability on IntelliJIdea

For purposes of testing, the android app was run on an emulator 

To enable the connection, [ngrok](https://ngrok.com/docs) was used to expose the client endpoints to the internet

##### Avaiiable Endpoints
>- /addItem
>- /updateItem
>- /deleteItem
>- /calcItemCost
>- /fetchAllItems
>- /fetchAllOrders
>- /addOrder

The endpoints can be reached through `localhost:8084` once the server and client are running

All the endpoints call tasks implemented on the serverImplemntation class [`FruitMartServiceImpl`](https://github.com/amina-k/webApp/blob/master/src/main/kotlin/server/FruitMartServiceImpl.kt) which extends the remote interface [`FruitMartRMI`](https://github.com/amina-k/webApp/blob/master/src/main/kotlin/server/FruitMartRMI.kt)

The server class [`FruitMartServer`](https://github.com/amina-k/webApp/blob/master/src/main/kotlin/server/FruitMartServer.kt) binds an  instance of the serverImplementation  class to a  registry running on port `1099` (default port)

The client class [`MainServlet`](https://github.com/amina-k/webApp/blob/master/src/main/kotlin/client/MainServlet.kt) accesses the same registry and calls the server classes

The server implementation communicates to the DB via the [firebase REST APIs](https://firebase.google.com/docs/database/rest/retrieve-data)

##### Server/Client packages:
 >- [jackson](https://github.com/FasterXML/jackson-module-kotlin)
 >- [khttp](https://khttp.readthedocs.io/)
 
##### Android packages:
 >- [jackson](https://github.com/FasterXML/jackson-module-kotlin)
 >- [fuel](https://khttp.readthedocs.io/)
 >- [gson](https://github.com/FasterXML/jackson-module-kotlin)

#### Demo
Click the links below to view the application demos, through the app as well as through REST APIs

[Android Demo](https://drive.google.com/file/d/1JnsvjYfwMawpsQS8qWmoMy4FywxoLUwE/view)

[Postman Demo](https://drive.google.com/file/d/190EwfHnWn3wRUR-t2Rk36aZPzLbsYiCx/view)

##### Contributors

[Lulu Karega](lulu.karega@strathmore.edu) 

[Mariam Mabinda](mariamabinda@gmail.com)