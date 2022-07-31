package site.caikun.poem

import com.google.gson.Gson
import java.io.File
import java.util.*

private val spider = PoemSpider()
private const val path = "./src/main/resources/"

fun main() {

    while (true) {
        println("\n====================================================================")
        println("1、爬取古诗")
        println("2、爬取名句")
        println("====================================================================\n")
        println("输入序号:")
        when (Scanner(System.`in`).nextInt()) {
            1 -> {
                runCatching {
                    val json = Gson().toJson(spider.poesy())
                    File(path + "poems.json").writeText(json)
                }.onFailure {
                    println("error")
                    it.message
                }
                println("finish")
            }

            2 -> {
                runCatching {
                    val json = Gson().toJson(spider.verse())
                    File(path + "verses.json").writeText(json)
                }.onFailure {
                    println("error")
                    it.message
                }
                println("finish")
            }
        }
    }
}