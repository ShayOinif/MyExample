import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.ktor.client.HttpClient
import io.ktor.http.encodedPath
import kotlinx.coroutines.flow.onCompletion
import kotlinx.rpc.client.withService
import kotlinx.rpc.serialization.json
import kotlinx.rpc.streamScoped
import kotlinx.rpc.transport.ktor.client.installRPC
import kotlinx.rpc.transport.ktor.client.rpc
import kotlinx.rpc.transport.ktor.client.rpcConfig

val client by lazy {
    HttpClient {
        installRPC()
    }
}

@Composable
fun App() {
    var serviceOrNull: MyService? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        serviceOrNull = client.rpc {
            url {
                host = "127.0.0.1"
                port = 8080
                encodedPath = "/api"
            }

            rpcConfig {
                serialization {
                    json()
                }
            }
        }.withService()
    }

    val service = serviceOrNull // for smart casting

    if (service != null) {
        val responses = remember { mutableStateListOf<String>() }

        LaunchedEffect(service) {
            streamScoped {
                service.subscribeToFlow()
                    .onCompletion {
                        responses.add("I am done")
                    }.collect { article ->
                        responses.add(article)
                    }
            }
        }

        MaterialTheme {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                responses.forEach {
                    Text(it)
                }
            }
        }
    }
}
