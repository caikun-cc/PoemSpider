package site.caikun.poem

import org.jsoup.Jsoup
import java.util.*

class PoemSpider {

    companion object {
        private const val website = "https://so.gushiwen.cn"

        private const val href = "href"
        private const val poesyURL = "https://so.gushiwen.cn/shiwens/"
        private const val verseURL = "https://so.gushiwen.cn/mingjus/"
    }

    private val poesyUrls = mutableListOf<String>()
    private val sentences = mutableListOf<Verse>()

    /**
     * 爬取古诗
     * @return 存放古诗的Poems对象
     */
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

            //爬取获取到的所有古诗链接
            if (poesyUrls.isNotEmpty()) {
                poesyUrls.forEach {
                    poems.add(details(it))
                }
            }
            println("sun:${poems.size}")
        }
        return Poems(poems)
    }

    /**
     * 爬取名句
     * @return 存放名句的Verses对象
     */
    fun verse(): Verses {
        val types = mutableMapOf<String, String>()

        //获取分类
        val html = Jsoup.parse(Http.request(verseURL))
        html.select("#type1 a").forEach {
            if (it.attr(href).isNotEmpty()) {
                val url = website + it.attr(href)
                types[it.text()] = url
            }
        }

        //获取每个分类下名句
        if (types.isNotEmpty()) {
            types.forEach { (k, v) ->
                println("$k:$v")
                sentence(v)
            }
            println("sun:${sentences.size}")
        }
        return Verses(sentences)
    }

    /**
     * 获取页面古诗链接，存放值列表中，递归的方式进行翻页
     * @param url 页面地址
     */
    private fun addUrls(url: String) {
        val html = Jsoup.parse(Http.request(url))
        html.select(".sons .cont :nth-child(2) a").forEach {
            if (it.attr(href).isNotEmpty()) {
                poesyUrls.add(website + it.attr(href))
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

    private fun sentence(url: String) {
        val html = Jsoup.parse(Http.request(url))
        html.select(".left .sons .cont").forEach {
            sentences.add(Verse().apply {
                id = UUID.randomUUID().toString().replace("-", "")
                content = it.text().split("——")[0].replace(" ", "")
                from = it.text().split("——")[1].replace(" ", "")
                link = website + it.select(":nth-child(1)").attr(href)
                type = html.select("#type1 .sright span")[0].text()
            })
        }

        //下一页
        val more = html.select(".amore")
        if (more.attr(href).isNotEmpty()) {
            sentence(more.attr(href))
        }
    }
}