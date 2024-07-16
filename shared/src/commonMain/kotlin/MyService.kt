import kotlinx.coroutines.flow.Flow
import kotlinx.rpc.RPC

interface MyService : RPC {
    suspend fun subscribeToFlow(): Flow<String>
}
