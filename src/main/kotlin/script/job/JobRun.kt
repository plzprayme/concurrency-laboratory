package script.job

import busyJob
import kotlinx.coroutines.*
import sliceInTo
import java.util.concurrent.atomic.AtomicLong

fun runBusyJob(numberOfCoroutine: Int): Unit {
    var sum : Long = 0
    sliceInTo(numberOfCoroutine).forEach { (s, e) ->
        sum += busyJob(s, e)
    }
//    println("1 THREAD RESULT: $sum")
//    return sum
}

fun runBusyJobWithMultiThread(numberOfCoroutine: Int) : Unit {
    var sum : Long = 0
    runBlocking {
        sliceInTo(numberOfCoroutine).forEachIndexed { i, (s, e) ->
            launch {
//                println("${Thread.currentThread()}: JOB ${i + 1} START")
                sum += busyJob(s, e)
//                println("${Thread.currentThread()}: JOB ${i + 1} END")
            }
        }
    }
//    println("1 THREAD RESULT $numberOfCoroutine COROUTINE RESULT: $sum")
//    return sum
}

fun runBusyJobWithMultiThread(
    numberOfCoroutine: Int,
    threadContext: ExecutorCoroutineDispatcher
) : Unit {
    var sum = AtomicLong()
    runBlocking {
        sliceInTo(numberOfCoroutine).forEachIndexed { i, (s, e) ->
            launch(threadContext) {
//                println("${Thread.currentThread()}: JOB ${i + 1} START")
                sum.getAndAdd(busyJob(s,e))
//                println("${Thread.currentThread()}: JOB ${i + 1} END")
            }
        }
    }
//    println("1 THREAD RESULT $numberOfCoroutine COROUTINE RESULT: $sum")
//    return sum
}