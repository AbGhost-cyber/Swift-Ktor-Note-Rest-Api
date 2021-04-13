package com.crushtech.swiftnote.routes

import com.crushtech.swiftnote.data.*
import com.crushtech.swiftnote.data.collections.model.Note
import com.crushtech.swiftnote.data.collections.response.SimpleResponse
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

/*
although this endpoint also falls under the "user" endpoint but i had to abstract it for readability
*/
fun Route.notesRouting() {
    route("/user/{uid}/note") {
        authenticate {
            get {
                val userId = call.parameters["uid"] ?: return@get call.respond(
                    OK,
                    SimpleResponse(false, "Missing or malformed id")
                )
                if (checkIfUserExistsById(userId)) {
                    val email = call.principal<UserIdPrincipal>()!!.name
                    val userNote = getUsersNote(email)
                    call.respond(OK, userNote)
                } else {
                    call.respond(
                        OK,
                        SimpleResponse(false, "user not found")
                    )
                }
            }
            get("{id}") {
                //get note by id
                val userId = call.parameters["uid"] ?: return@get call.respond(
                    OK,
                    SimpleResponse(false, "Missing or malformed id")
                )
                if (checkIfUserExistsById(userId)) {
                    val id = call.parameters["id"] ?: return@get call.respond(
                        OK,
                        SimpleResponse(false, "Missing or malformed id")
                    )
                    if (checkIfNoteExists(id)) {
                        val note = getNoteById(id)
                        call.respond(OK, note)
                    } else {
                        call.respond(
                            OK,
                            SimpleResponse(false, "note not found")
                        )
                    }
                } else {
                    call.respond(
                        OK,
                        SimpleResponse(false, "user not found")
                    )
                }
            }
            post {
                //add note
                val userId = call.parameters["uid"] ?: return@post call.respond(
                    OK,
                    SimpleResponse(false, "Missing or malformed id")
                )
                val note = try {
                    call.receive<Note>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (checkIfUserExistsById(userId)) {
                    if (saveNote(note)) {
                        call.respond(OK)
                    } else {
                        call.respond(Conflict)
                    }
                } else {
                    call.respond(
                        OK,
                        SimpleResponse(false, "user not found")
                    )
                }
            }
            delete("{id}"){
                //delete note
                val userId = call.parameters["uid"] ?: return@delete call.respond(
                    OK,
                    SimpleResponse(false, "Missing or malformed id")
                )
                if (checkIfUserExistsById(userId)) {
                    val noteId = call.parameters["id"] ?: return@delete call.respond(
                        OK,
                        SimpleResponse(false, "Missing or malformed id")
                    )
                    if (checkIfNoteExists(noteId)) {
                       if(deleteNote(noteId)){
                           call.respond(OK, SimpleResponse(true, "note deleted"))
                       }
                    } else {
                        call.respond(
                            OK,
                            SimpleResponse(false, "note not found")
                        )
                    }
                } else {
                    call.respond(
                        OK,
                        SimpleResponse(false, "user not found")
                    )
                }

            }
        }
    }
}

fun Application.registerNotesRoutes(){
    routing {
        notesRouting()
    }
}
