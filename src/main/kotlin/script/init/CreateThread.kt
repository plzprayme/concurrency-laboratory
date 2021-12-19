import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.newFixedThreadPoolContext

fun createThreads(numberOfThreads: Int): ExecutorCoroutineDispatcher {
    return newFixedThreadPoolContext(numberOfThreads - 1, "New Threads")
}