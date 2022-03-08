data class Note(
    val noteID: Int,
    val date: Long,
    var title: String,
    var text: String,
    val comments: MutableList<Comment>
)