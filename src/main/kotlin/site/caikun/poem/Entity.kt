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

data class Verse(
    var id: String? = "",
    var content: String? = "",
    var from: String? = "",
    var link: String? = "",
    var type: String? = "",
)

data class Verses(
    var verses: List<Verse>
)