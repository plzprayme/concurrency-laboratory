import script.job.runBusyJob
import script.job.runBusyJobWithMultiThread
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

fun main(args: Array<String>) {
    // 싱글 스레드
    iterFiveSingle(1)
    iterFiveSingle(2)
    iterFive(4)
    iterFive(8)
    iterFive(16)
    iterFive(32)
    iterFive(64)
    iterFive(128)

    // 스레드 생성시간 포함
    iterFiveMultiMakeThread(1, 2)
    iterFiveMultiMakeThread(2, 2)
    iterFiveMultiMakeThread(4, 2)
    iterFiveMultiMakeThread(4, 4)
    iterFiveMultiMakeThread(8, 4)
    iterFiveMultiMakeThread(16, 8)
    iterFiveMultiMakeThread(32, 16)
    iterFiveMultiMakeThread(64, 32)
    iterFiveMultiMakeThread(128, 64)
    iterFiveMultiMakeThread(256, 128)
    iterFiveMultiMakeThread(512, 256)
    iterFiveMultiMakeThread(1024, 512)
    iterFiveMultiMakeThread(2048, 1024)
    iterFiveMultiMakeThread(4096, 2048)

    // 멀티 스레드
    iterFiveMulti(1, 2)
    iterFiveMulti(2, 2)
    iterFiveMulti(4, 2)
    iterFiveMulti(4, 4)
    iterFiveMulti(8, 4)
    iterFiveMulti(16, 8)
    iterFiveMulti(32, 16)
    iterFiveMulti(64, 32)
    iterFiveMulti(128, 64)
    iterFiveMulti(256, 128)
    iterFiveMulti(512, 256)
    iterFiveMulti(1024, 2048)
}


val PRINT_FORMAT = "%s THREAD RESULT %s COROUTINE RESULT: %s";

fun iterFive(count : Int) {
    val time = measureAvg {
        runBusyJob(count)
    }
    println(String.format(PRINT_FORMAT, 1, count, time))
}

fun iterFiveSingle(count : Int) {
    val time = measureAvg {
        runBusyJobWithMultiThread(count)
    }
    println(String.format(PRINT_FORMAT, 1, count, time))
}

@OptIn(ExperimentalTime::class)
fun iterFiveMulti(cc: Int, tc: Int) {
    val threadPoolContext = createThreads(tc)

    val time = measureAvg {
        runBusyJobWithMultiThread(cc, threadPoolContext)
    }
    println(String.format(PRINT_FORMAT, tc, cc, time))

//    println("1 THREAD RESULT $numberOfCoroutine COROUTINE RESULT: $sum")
//    var time = measureTime {  };
//    repeat(5) {
//        time += measureTime { runBusyJobWithMultiThread(cc, threadPoolContext) }
//    }
//    println(time / 5)
//    println("AVG")

}

fun iterFiveMultiMakeThread(cc: Int, tc: Int) {
    val time = measureAvg {
        runBusyJobWithMultiThread(cc, createThreads(tc))
    }

    println(String.format(PRINT_FORMAT, tc, cc, time))
}

@OptIn(ExperimentalTime::class)
fun measureAvg(job: () -> Unit): Duration {
    var time = measureTime {  }

    repeat(5) {
        time += measureTime { job() }
    }


    return time / 5;
}
