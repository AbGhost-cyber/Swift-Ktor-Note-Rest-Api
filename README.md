# Swift-Ktor-Note-Rest-Api
Backend for Swift Note App

#HOW TO USE:
- install intellij, clone this repo and build project.
- currently this server runs on localhost.
- you can also use postman to test each endpoint before integrating on client side.
</br>

#SIMPLE NOTE REST API MADE WITH KTOR AND MONGO DB KTOR LIBRARY KMONGO
<br>this repository demonstrates how to build an http rest api with ktor, currently there are two routes namely: note route and user route, below are the respective api end points.

#USER ROUTE:

| END POINTS      | METHOD        | FUNCTION |
| ------------- |:-------------:| :-----|
| /user/{uid}      | GET | this endpoint returns a user excluding confidential informations |
| /user/login     | POST     |   login endpoint |
| /user | POST      |  this endpoint registers the user
| /user/{uid} | DELETE      |    this endpoint deletes the user from the mongo db |


#NOTE ROUTE:
| END POINTS      | METHOD        | FUNCTION |
| ------------- |:-------------:| :-----|
| /user/{uid}/note      | GET | this endpoint returns all notes for the specified {uid}|
| /user/{uid}/note?pin=true   | GET    | this endpoint returns all pinned notes for the specified {uid} |
| /user/{uid}/note/{id} |GET     | this endpoint returns a note if the speciifed note {id} is valid|
| /user/{uid}/note | POST   |  this endpoint adds a note to mongo db|
| /user/{uid}/note/pin/{id} | POST     |    method:post this endpoint adds a note to a user's list of pinned notes or removes if already pinned(currently restricted to 4 pinned notes per user). |
| /user/{uid}/note/{id} | DELETE      |    this endpoint deletes a note from mongo db |



