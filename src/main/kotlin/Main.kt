import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import java.lang.Thread.sleep
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

fun main(args: Array<String>) {

    printSpendTime {
        when (args[0]) {
            "1t" -> singleThread(busyJob)
            "1ts" -> singleThreadSplitVersion(busyJob)
            "1t1c" -> s1c1(busyJob)
            "1t2c" -> s1c2(busyJob)
            "1t3c" -> t1c3(busyJob)
            "1t4c" -> t1c4(busyJob)
            "2t2c" -> t2c2(busyJob)
            "l1t2c" -> s1c2(lazyJob)
        }
        Thread.currentThread().threadGroup.list()
    }
}

fun singleThread(job: (Int, Int) -> Int) {
    // 2.6초
    val sum = job(0, Int.MAX_VALUE)
    println("1 THREAD RESULT: $sum")
}

fun singleThreadSplitVersion(job: (Int, Int) -> Int) {
    // JOB을 나눴다. 5.2초
    var sum = 0;
    sum += job(Int.MIN_VALUE, 0)
    sum += job(0, Int.MAX_VALUE)
    println("1 THREAD 1 COROUTINE SPLITED JOB RESULT: $sum")
}

fun s1c1(job: (Int, Int) -> Int) {
    // 2.6초
    runBlocking {
        launch {
            val sum = job(Int.MIN_VALUE, Int.MAX_VALUE)
            println("1 THREAD 1 COROUTINE RESULT: $sum")
        }
    }

}

fun s1c2(job: (Int, Int) -> Int) {
    // 싱글 스레드 더블 코루틴: 2.1초
    var sum = 0

    runBlocking {

        launch {
            println("${Thread.currentThread()}: JOB 1 START")
            sum += job(Int.MIN_VALUE, 0)
            println("${Thread.currentThread()}: JOB 1 END")
        }

        launch {
            println("${Thread.currentThread()}: JOB 1 START")
            sum += job(0, Int.MAX_VALUE)
            println("${Thread.currentThread()}: JOB 1 END")
        }

    }

    println("1 THREAD 2 COROUTINE RESULT: $sum")

}

fun t1c3(job: (Int, Int) -> Int) {
    // 1.86
    var sum = 0

    runBlocking {

        launch {
            println("${Thread.currentThread()}: JOB 1 START")
            sum += job(Int.MIN_VALUE, -747_483_648)
            println("${Thread.currentThread()}: JOB 1 END")
        }

        launch {
            println("${Thread.currentThread()}: JOB 2 START")
            sum += job(-747_483_648, 747_483_648)
            println("${Thread.currentThread()}: JOB 2 END")
        }

        launch {
            println("${Thread.currentThread()}: JOB 3 START")
            sum += job(747_483_648, Int.MAX_VALUE)
            println("${Thread.currentThread()}: JOB 3 END")
        }

    }

    println("1 THREAD 3 COROUTINE RESULT: $sum")
}

fun t1c4(job: (Int, Int) -> Int) {
    // 1.73
    var sum = 0
    runBlocking {

        launch {
            println("${Thread.currentThread()}: JOB 1 START")
            sum += job(Int.MIN_VALUE, -1_147_483_648)
            println("${Thread.currentThread()}: JOB 1 END")
        }

        launch {
            println("${Thread.currentThread()}: JOB 2 START")
            sum += job(-1_147_483_648, 0)
            println("${Thread.currentThread()}: JOB 2 END")
        }

        launch {
            println("${Thread.currentThread()}: JOB 3 START")
            sum += job(0, 1_147_483_648)
            println("${Thread.currentThread()}: JOB 3 END")
        }

        launch {
            println("${Thread.currentThread()}: JOB 4 START")
            sum += job(1_147_483_648, Int.MAX_VALUE)
            println("${Thread.currentThread()}: JOB 4 END")
        }

    }

    println("1 THREAD 4 COROUTINE RESULT: $sum")
}

fun t2c2(job: (Int, Int) -> Int) {
    // 더블 스레드 더블 코루틴: 1.4초
    var sum = 0

    runBlocking {

        launch {
            println("${Thread.currentThread()}: FIRST JOB START")
            sum += job(Int.MIN_VALUE, 0)
            println("${Thread.currentThread()}: FIRST JOB END")
        }

        launch(newSingleThreadContext("NewThread")) {
            println("${Thread.currentThread()}: FIRST JOB START")
            sum += job(0, Int.MAX_VALUE)
            println("${Thread.currentThread()}: FIRST JOB END")
        }

    }

    println("1 THREAD 2 COROUTINE RESULT: $sum")

}

@OptIn(ExperimentalTime::class)
fun printSpendTime(job: () -> Unit) {
    val duration = measureTime { job() }
    println("Spend Time: $duration")
}

val busyJob = { start: Int, endInclusive: Int ->
    var count = 0
    for (i in start..endInclusive) {
        count += i
    }
    count
}

val lazyJob = { _: Int, _: Int ->
    // 도커 정의 필요
    sleep(5_000)
    1
}
