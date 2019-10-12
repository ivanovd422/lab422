package main

import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.*
import io.ktor.routing.*

fun main(args: Array<String>) {
    // Init Ktor
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    routing {
        route("/tasks", Route::taskRoute)

        get("") {
            call.respond("hello world")
        }

        get("/123") {
            call.respond("321")
        }

        get("/clear") {
            Tasks.clearTasks()
            call.respond(HttpStatusCode.OK)
        }
    }

    install(ContentNegotiation) {
        gson { }
    }

}

// Custom routing extension function
fun Route.taskRoute() {
    // return existing tasks
    get("/") {
        call.respond(Tasks.getTasks())
    }
    // Add new Task
    post("/") {
        val task = call.receive<String>()
        Tasks.addTask(task)
        call.respond(HttpStatusCode.Created)
    }

    get("/clear") {
        Tasks.clearTasks()
        call.respond(HttpStatusCode.OK)
    }
}
