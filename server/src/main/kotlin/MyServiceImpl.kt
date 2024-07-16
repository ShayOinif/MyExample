import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.seconds

class MyServiceImpl(override val coroutineContext: CoroutineContext) : MyService {
    override suspend fun subscribeToFlow(): Flow<String> {
        return flow {
            repeat(10) {
                delay(1.seconds)
                emit("Response: $it")
            }
        }
    }
}
