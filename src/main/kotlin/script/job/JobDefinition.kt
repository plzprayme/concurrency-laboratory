val busyJob = { start: Long, endInclusive: Long ->
    var count : Long = 0
    for (i in start until endInclusive) {
        count += i
    }
    count
}

val lazyJob = { _: Long, _: Long ->
    // 도커 정의 필요
    Thread.sleep(5_000)
    1
}