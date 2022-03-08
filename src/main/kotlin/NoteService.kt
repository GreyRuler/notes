import kotlinx.datetime.Clock

class NoteService {

    val listNotes = mutableListOf<Note>()
    private var noteID = 0
    private var commentID = 0

    fun add(title: String, text: String): Int {
        listNotes.add(Note(
            noteID = ++noteID,
            date = Clock.System.now().epochSeconds,
            title, text, mutableListOf()))
        return noteID
    }

    fun createComment(noteID: Int, message: String): Int {
        val note = listNotes.find { note -> note.noteID == noteID }
        return if (note != null) {
            note.comments.add(Comment(
                commentID = ++commentID,
                noteID = noteID,
                date = Clock.System.now().epochSeconds,
                message = message,
                isDelete = false)
            )
            commentID
        } else {
            throw NoteNotFoundException("Note not found")
        }
    }

    fun delete(noteID: Int): Boolean {
        val index = listNotes.indexOfFirst { note -> note.noteID == noteID }
        return if (index != -1) {
            listNotes.removeAt(index)
            true
        } else {
            throw NoteNotFoundException("Note not found")
        }
    }

    fun deleteComment(commentID: Int): Boolean {
        listNotes.forEach {
            note: Note -> val index = note.comments.indexOfFirst { comment -> comment.commentID == commentID }
            if (index != -1) {
                note.comments[index].isDelete = true
                return true
            }
        }
        throw CommentNotFoundException("Comment not found")
    }

    fun edit(noteID: Int, title: String, text: String): Boolean {
        val note = listNotes.find { note -> note.noteID == noteID }
        return if (note != null) {
            note.title = title
            note.text = text
            true
        } else {
            throw NoteNotFoundException("Note not found")
        }
    }

    fun editComment(commentID: Int, message: String): Boolean {
        listNotes.forEach {
                note: Note -> val index = note.comments.indexOfFirst { comment -> comment.commentID == commentID }
            if (index != -1) {
                note.comments[index].message = message
                return true
            }
        }
        throw CommentNotFoundException("Comment not found")
    }

    fun get(noteIDS: List<Int>, offset: Int = 0, count: Int = 20, sort: Int = 0): List<Note> {
        val resultNotes = listNotes.slice(offset until listNotes.size).take(count).filter { note -> noteIDS.contains(note.noteID)  }
        return when(sort) {
            0 -> resultNotes.sortedBy { note -> note.date }
            1 -> resultNotes.sortedByDescending { note -> note.date }
            else -> throw SortException("Sort takes only two values 0 or 1")
        }
    }

    fun getById(noteID: Int): Int? {
        return listNotes.find { note -> note.noteID == noteID }?.noteID
    }

    fun getComments(noteID: Int, sort: Int = 0, offset: Int = 0, count: Int = 20): List<Comment>? {
        val comments = listNotes.find { note -> note.noteID == noteID }?.comments
        val result = comments?.slice(offset until comments.size)?.take(count)
        return when(sort) {
            0 -> result?.sortedBy { comment -> comment.date }
            1 -> result?.sortedByDescending { comment -> comment.date }
            else -> throw SortException("Sort takes only two values 0 or 1")
        }
    }

    fun restoreComment(commentID: Int): Boolean {
        listNotes.forEach {
                note: Note -> note.comments.find { comment -> comment.commentID == commentID }.let { comment -> comment?.isDelete = false }
        }
        return true
    }
}