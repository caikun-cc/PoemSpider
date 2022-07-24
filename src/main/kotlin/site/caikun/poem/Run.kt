package site.caikun.poem

fun main() {
    val poems = PoemSpider().poesy()
    println(poems.poems.size)
}