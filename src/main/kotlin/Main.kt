import kotlinx.coroutines.*
import script.job.runBusyJob
import script.job.runBusyJobWithCoroutine
import script.log.printAllThreads
import script.log.printSpendTime

fun main(args: Array<String>) {

    // args[0] = thread count
    // args[1] = coroutine count
    // args[2] = l == lazy or {empty string} == busy
    val threadCount = args[0].toInt()
    val coroutineCount = args[1].toInt()
    val isLazyJob = args.size == 3 && args[2] == "l"

    val threadPoolContext = createThreads(threadCount)

    printSpendTime {
        if (isLazyJob) {
            // TODO CPU를 사용하지 않는 작업들 테스트

        } else {
            if (threadCount == 1) {
                if (coroutineCount == 0) {
                    // 1 스레드 1 코루틴
                    runBusyJob(1)
                    runBusyJob(2)
                } else {
                    // 1 스레드 N 코루틴
                    runBusyJobWithCoroutine(coroutineCount)
                }
            } else  {
                // 멀티 스레드 1~N 코루틴
                runBusyJobWithCoroutine(coroutineCount, threadPoolContext)
            }
        }
        printAllThreads()
    }
}