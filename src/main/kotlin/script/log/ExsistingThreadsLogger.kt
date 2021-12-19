package script.log

fun printAllThreads() {
    Thread.currentThread().threadGroup.list()
}