private const val MAX_END_INCLUSIVE = 2_000_000_000

fun sliceInTo(number: Int): ArrayList<Pair<Int, Int>> {
    val sliceStep = MAX_END_INCLUSIVE / number
    val slicedRanges = arrayListOf<Pair<Int, Int>>()
    for (start in 0 until  MAX_END_INCLUSIVE step sliceStep) {
        slicedRanges.add(Pair(start, start + sliceStep))
    }
    return slicedRanges
}