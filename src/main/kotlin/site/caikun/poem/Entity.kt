package site.caikun.poem

data class Poem(
    var id: String? = "",
    var author: String? = "",
    var title: String? = "",
    var content: String? = "",
    var dynasty: String? = "",
    var link: String? = "",
)

data class Poems(
    var poems: List<Poem>
)