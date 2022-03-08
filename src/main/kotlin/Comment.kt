data class Comment(
    val commentID: Int,
    val noteID: Int,
    val date: Long,
    var message: String,
    var isDelete: Boolean
)