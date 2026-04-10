import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.net.HttpURLConnection
import java.net.URL
import java.io.OutputStreamWriter

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    embeddedServer(Netty, port = port) {
        routing {
            get("/") { call.respondText("Bot is Alive!") }
            get("/post-news") {
                val status = postToGroupMe("Wacky Bot Triggered via Cron!")
                call.respondText("Status: $status")
            }
        }
    }.start(wait = true)
}

fun postToGroupMe(msg: String): String {
    return try {
        val url = URL("https://api.groupme.com/v3/bots/post")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/json")
        val json = "{\"bot_id\": \"980cad6d49e3be7162e3d23c82\", \"text\": \"$msg\"}"
        OutputStreamWriter(conn.outputStream).use { it.write(json) }
        "Code ${conn.responseCode}"
    } catch (e: Exception) { "Error: ${e.message}" }
}
