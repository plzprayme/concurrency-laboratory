private const val MAX_END_INCLUSIVE = 6_000_000_000

fun sliceInTo(number: Int): ArrayList<Pair<Long, Long>> {
    validate(number)
    val sliceStep : Long = MAX_END_INCLUSIVE / number
    val slicedRanges = arrayListOf<Pair<Long, Long>>()
    for (start in 0.toLong() until  MAX_END_INCLUSIVE step sliceStep) {
        slicedRanges.add(Pair(start, start + sliceStep))
    }
    return slicedRanges
}

private fun validate(number : Int) {
    if (number == 1 || number % 2 == 0) {
        return;
    }

    throw IllegalArgumentException("코루틴의 수는 2 이상의 자연수 짝수 여야 합니다.")
}
