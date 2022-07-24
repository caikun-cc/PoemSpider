package site.caikun.poem

import org.jsoup.Jsoup
import java.util.*

class PoemSpider {

    companion object {
        private const val website = "https://so.gushiwen.cn"

        private const val href = "href"
        private const val poesyURL = "https://so.gushiwen.cn/shiwens/"
    }

    private val urls = mutableListOf<String>()

    fun poesy(): Poems {
        val poems = mutableListOf<Poem>()
        val authors = mutableMapOf<String, String>()

        //获取作者
        val html = Jsoup.parse(Http.request(poesyURL))
        html.select("#type2 a").forEach {
            if (it.attr(href).isNotEmpty()) {
                val url = website + it.attr(href)
                authors[it.text()] = url
            }
        }

        //获取作者下古诗链接
        if (authors.isNotEmpty()) {
            authors.forEach { (k, v) ->
                println("\n\n==============================================================")
                println("$k:$v")
                println("==============================================================\n\n")
                addUrls(v)
            }

            println(urls.size)
            //爬取获取到的所有古诗链接
            if (urls.isNotEmpty()) {
                urls.forEach {
                    poems.add(details(it))
                }
            }
        }
        return Poems(poems)
    }

    private fun addUrls(url: String) {
        val html = Jsoup.parse(Http.request(url))
        html.select(".sons .cont :nth-child(2) a").forEach {
            if (it.attr(href).isNotEmpty()) {
                urls.add(website + it.attr(href))
                println(it.text() + ":" + website + it.attr(href))
            }
        }

        //下一页
        val more = html.select(".amore")
        if (more.attr(href).isNotEmpty()) {
            addUrls(website + more.attr(href))
        }
    }

    private fun details(url: String): Poem {
        println(url)
        val html = Jsoup.parse(Http.request(url))
        val poem = Poem().apply {
            link = url
            id = UUID.randomUUID().toString().replace("-", "")
            author = html.select(".source a")[0].text()
            title = html.select("h1")[0].text()
            content = html.select(".contson")[0].text()
                .replace(" ", "")
                .replace("\\n", "")
                .replace("　　", "")
            dynasty = html.select(".source a")[1].text()
                .replace("〔", "")
                .replace("〕", "")
        }
        println(poem)
        return poem
    }
}