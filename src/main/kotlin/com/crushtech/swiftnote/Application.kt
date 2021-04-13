package com.crushtech.swiftnote

import com.crushtech.swiftnote.data.checkPasswordForUser
import com.crushtech.swiftnote.routes.registerNotesRoutes
import com.crushtech.swiftnote.routes.registerUserRoutes
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(Authentication) {
        configureAuth()
    }
    registerUserRoutes()
    registerNotesRoutes()
}

private fun Authentication.Configuration.configureAuth() {
    basic {
        realm = "Swift Notes Server"
        validate { credentials ->
            val email = credentials.name
            val password = credentials.password
            checkPasswordForUser(email, password)
            UserIdPrincipal(email)
        }
    }
}



