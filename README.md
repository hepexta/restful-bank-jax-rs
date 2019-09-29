# restful-bank-jax-rs

A RESTful API for money transfers between internal accounts.

**HOWTO:**

A simple test can be look like this.

`cd transfer-rest-api`

`mvn clean install`

`cd target/`

`java -jar tomcat-embedded-example-0.0.1.jar -DPORT=8080 -DENV=db`
 
 alternative: `mvn exec:java -DPORT=8085 -DENV=db -f pom.xml`
 
 Note: both -DPORT=8080 and -DENV=db are optional. 
* PORT - default value: 8080
* ENV - specifies persistence layer. Cache or db. default value: db
 
 Now we can initialize our database: 
  ```
  GET http://localhost:8080/dbinit
  ```
 
 To check the initialization let's ask our service to return data. 
   ```
   GET http://localhost:8080/client/list
   ```
 ```
  [
   {
     "id": "1",
     "name": "Bank"
   },
   {
     "id": "2",
     "name": "John Smith"
   },
   {
     "id": "3",
     "name": "Peter Parker"
   },
   {
     "id": "4",
     "name": "Keanu Reeves"
   }
 ]
 ```
 
 
 ```
    GET http://localhost:8080/account/list
 ```
 ```
 [
   {
     "id": "1",
     "number": "CASH ACCOUNT",
     "client": {
       "id": "1",
       "name": "Bank"
     },
     "balance": 100.0000
   },
   {
     "id": "2",
     "number": "EXPENSES",
     "client": {
       "id": "1",
       "name": "Bank"
     },
     "balance": 10000.0000
   },
   {
     "id": "3",
     "number": "ACC02",
     "client": {
       "id": "2",
       "name": "John Smith"
     },
     "balance": 100.0000
   },
   {
     "id": "4",
     "number": "ACC03",
     "client": {
       "id": "3",
       "name": "Peter Parker"
     },
     "balance": 100.0000
   },
   {
     "id": "5",
     "number": "ACC04",
     "client": {
       "id": "4",
       "name": "Keanu Reeves"
     },
     "balance": 200.0000
   }
 ]
 ```  
 Let's add new client into Bank System.
 ```
POST http://localhost:8080/client/insert
Accept: application/json
Content-Type: application/json

{"name":"Sergei Kuznetsov"}
 ```
 In response we can find an ID of created client:
 
 `GET http://localhost:8080/client/fetchbyid/5`
 
 Let's create our first account:
  ```
  POST http://localhost:8080/account/insert
   Accept: application/json
   Content-Type: application/json
   
   {"number":"ACCSERGEI01", "client":{"id":"5"}, "balance":"1000"}
 ```
 
 The ID of this account is in response:
 
 `GET http://localhost:8080/account/find/6`
 
 Let's add new account:
 ```
POST http://localhost:8080/client/insert
Accept: application/json
Content-Type: application/json

{"name":"Ryan Reynolds"}

POST http://localhost:8080/account/insert
   Accept: application/json
   Content-Type: application/json
   
   {"number":"ACCRYAN01", "client":{"id":"6"}, "balance":"0"}
 ```
 
 And now we can transfer some money from account to account
 ```
 POST http://localhost:8080/transfer/execute
 {"sourceAccountId":"6", "destAccountId":"7", "operDate":"2019-09-04", "amount":1000, "comment":"Transfer for charity"}
 ```

 And then both can see this transfer in their lists:
 
 `GET http://localhost:8080/transfer/findbyaccountid/6`
 
 `GET http://localhost:8080/transfer/findbyaccountid/7`
 
 ------------------------------------
 
 Whole list of available operations:
 
 `GET http://localhost:8080/client/list`
 
 `GET http://localhost:8080/client/findbyid/{id}`
 
 `POST http://localhost:8080/client/insert/`
 
 `POST http://localhost:8080/client/modify/{id}`
 
 `DELETE http://localhost:8080/client/delete/{id}`
 
 `GET http://localhost:8080/account/list`
 
  `GET http://localhost:8080/account/findbyid/{id}`
  
  `POST http://localhost:8080/account/insert/`
  
  `POST http://localhost:8080/account/modify/{id}`
  
  `DELETE http://localhost:8080/account/delete/{id}`
  
  `POST http://localhost:8080/account/deposit`
  
  `POST http://localhost:8080/account/withdrawal`
  
  `GET http://localhost:8080/transfer/findbyaccountid/{id}`
  
  `POST http://localhost:8080/transfer/execute`
  
 