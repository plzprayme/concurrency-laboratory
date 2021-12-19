val busyJob = { start: Int, endInclusive: Int ->
    var count = 0
    for (i in start until endInclusive) {
        count += i
    }
    count
}

val lazyJob = { _: Int, _: Int ->
    // 도커 정의 필요
    Thread.sleep(5_000)
    1
}