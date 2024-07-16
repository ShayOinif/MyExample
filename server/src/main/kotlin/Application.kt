import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import kotlinx.rpc.serialization.json
import kotlinx.rpc.transport.ktor.server.RPC
import kotlinx.rpc.transport.ktor.server.rpc

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    install(RPC)
    routing {
        rpc("/api") {
            rpcConfig {
                serialization {
                    json()
                }
            }

            registerService<MyService> { ctx -> MyServiceImpl(ctx) }
        }
    }
}
