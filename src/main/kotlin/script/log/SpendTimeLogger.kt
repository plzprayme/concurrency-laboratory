package script.log

import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun printSpendTime(job: () -> Unit) {
    val duration = measureTime { job() }
    println("Spend Time: $duration")
}