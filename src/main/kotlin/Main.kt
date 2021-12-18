import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import java.lang.Thread.sleep
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

fun main(args: Array<String>) {
    Thread.currentThread().threadGroup.list()

    printSpendTime {
        when (args[0]) {
            "st" -> singleThread(busyJob)
            "stsc" -> singleThreadSingleCoroutine(busyJob)
            "stscs" -> singleThreadSplitVersion(busyJob)
            "stdc" -> singleThreadDoubleCoroutines(busyJob)
            "dtdc" -> doubleThreadDoubleCoroutines(busyJob)
            "lstdc" -> singleThreadDoubleCoroutines(lazyJob)
        }
    }
}

fun singleThread(job: (Int, Int) -> Int) {
    // 3.0초
    val sum = job(Int.MIN_VALUE, Int.MAX_VALUE)
    println("SINGLE THREAD RESULT: $sum")
}

fun singleThreadSplitVersion(job: (Int, Int) -> Int) {
    // JOB을 나누면 5.1초정도 걸리네..
    var sum = 0;
    sum += job(Int.MIN_VALUE, 0)
    sum += job(0, Int.MAX_VALUE)
    println("SINGLE THREAD SINGLE COROUTINE SPLIT VERSION RESULT: $sum")
}

fun singleThreadSingleCoroutine(job: (Int, Int) -> Int) {
    // 3.1초
    runBlocking {
        launch {
            val sum = job(Int.MIN_VALUE, Int.MAX_VALUE)
            println("SINGLE THREAD SINGLE COROUTINE RESULT: $sum")
        }
    }

}

fun singleThreadDoubleCoroutines(job: (Int, Int) -> Int) {
    // 싱글 스레드 더블 코루틴: 5.5초
    var sum = 0

    runBlocking {

        launch {
            println("${Thread.currentThread()}: FIRST LAUNCH START")
            sum += job(Int.MIN_VALUE, 0)
            println("${Thread.currentThread()}: FIRST LAUNCH END")
        }

        launch {
            println("${Thread.currentThread()}: SECOND LAUNCH START")
            sum += job(0, Int.MAX_VALUE)
            println("${Thread.currentThread()}: SECOND LAUNCH END")
        }

    }

    println("SINGLE THREAD DOUBLE COROUTINE RESULT: $sum")

}

fun doubleThreadDoubleCoroutines(job: (Int, Int) -> Int) {
    // 더블 스레드 더블 코루틴: 1.69초
    var sum = 0

    runBlocking {

        launch {
            println("${Thread.currentThread()}: FIRST LAUNCH START")
            sum += job(Int.MIN_VALUE, 0)
            println("${Thread.currentThread()}: FIRST LAUNCH END")
        }

        launch(newSingleThreadContext("NewThread")) {
            println("${Thread.currentThread()}: FIRST LAUNCH START")
            sum += job(0, Int.MAX_VALUE)
            println("${Thread.currentThread()}: FIRST LAUNCH END")
        }

    }

    println("SINGLE THREAD DOUBLE COROUTINE RESULT: $sum")

}

@OptIn(ExperimentalTime::class)
fun printSpendTime(job: () -> Unit) {
    val duration = measureTime { job() }
    println("Spend Time: $duration")
}

val busyJob = { start: Int, endInclusive: Int ->
    IntRange(start, endInclusive).sumOf { it }
}

val lazyJob = { _: Int, _: Int ->
    sleep(5_000)
    1
}
