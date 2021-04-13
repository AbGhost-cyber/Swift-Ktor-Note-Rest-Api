package com.crushtech.swiftnote.routes

import com.crushtech.swiftnote.data.*
import com.crushtech.swiftnote.data.collections.model.User
import com.crushtech.swiftnote.data.collections.response.SimpleResponse
import com.crushtech.swiftnote.requests.SignInRequest
import com.crushtech.swiftnote.security.getHashWithSalt
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

/*
custom route to group everything that falls under user endpoint
*/
fun Route.userRouting() {
    route("/user") {

        get("{uid}") {
            //get user by id
            val id =
                call.parameters["uid"] ?: return@get call.respond(
                    OK,
                    SimpleResponse(false, "Missing or malformed id")
                )
            if (checkIfUserExistsById(id)) {
                val user = getUser(id)
                val userWithHiddenDetails = User(user.email, "******", user.username, user.uid)
                call.respond(OK, userWithHiddenDetails)
            } else {
                call.respond(
                    OK,
                    SimpleResponse(false, "no user with id $id")
                )
            }

        }
        post("/login") {
            val request = try {
                call.receive<SignInRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(BadRequest)
                return@post
            }
            if (checkIfUserExistsByEmail(request.email)) {
                val passwordIsCorrect = checkPasswordForUser(request.email, request.password)
                if (passwordIsCorrect) {
                    val username = getUsernameFromEmail(request.email)
                    call.respond(OK, SimpleResponse(true, username))
                } else {
                    call.respond(OK, SimpleResponse(false, "the email or password is incorrect"))
                }
            } else {
                call.respond(OK, SimpleResponse(false, "This user does not exist"))
            }

        }
        post {
            //register user
            val request = try {
                call.receive<User>()
            } catch (e: ContentTransformationException) {
                call.respond(BadRequest)
                return@post
            }
            if (checkIfUserExistsByEmail(request.email)) {
                call.respond(
                    OK, SimpleResponse(
                        false,
                        "A user with this email already exist"
                    )
                )
            } else if (registerUser(
                    User(
                        request.email,
                        getHashWithSalt(request.password), request.username
                    )
                )
            ) {
                call.respond(
                    OK, SimpleResponse(
                        true,
                        "Account Created Successfully"
                    )
                )
            }
        }
        delete("{id}") {
            //delete user
            val id = call.parameters["id"] ?: return@delete call.respond(BadRequest)
            if (checkIfUserExistsById(id)) {
                if (deleteUser(id)) {
                    call.respond(OK, SimpleResponse(true, "User deleted successfully"))
                }
            } else {
                call.respond(OK, SimpleResponse(false, "User not found"))
            }
        }
    }
}

fun Application.registerUserRoutes() {
    routing {
        userRouting()
    }
}