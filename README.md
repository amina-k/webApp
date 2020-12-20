
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
For purposes of testing, the app was run on an emulator 
To enable the connection, [ngrok](https://ngrok.com/docs) was used to expose the client endpoints to the internet

### Avaiiable Endpoints
- /addItem
- /updateItem
- /deleteItem
- /calcItemCost
- /fetchAllItems
- /fetchAllOrders
- /addOrder

All the above call tasks implemented on the serverImplemntation class `FruitMartServiceImpl` which extends the remote interface `FruitMartRMI`

The server class `FruitMartServer` binds an  instance of the serverImplementation  class to a  registry running on port `1099` (default port)

The client accesses the same registry and calls the server classes

The server implementation communicates to the DB via the [firebase REST APIs](https://firebase.google.com/docs/database/rest/retrieve-data)

#### Demo
Click the links below to view the application demos, through the app as well as through REST APIs

[Android Demo](https://drive.google.com/file/d/1fwI26igKLKEO2Qn2wNPLoOEAI6mjYSwd/view)


[Postman Demo](https://drive.google.com/file/d/190EwfHnWn3wRUR-t2Rk36aZPzLbsYiCx/view)