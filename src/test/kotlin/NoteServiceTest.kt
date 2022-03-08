import org.junit.Test

import org.junit.Assert.*

class NoteServiceTest {

    @Test
    fun add_shouldAddNote() {
        val title = "Hello, world"
        val text = "Тестовый текст"
        val noteService = NoteService()

        val result = noteService.add(title, text)
        val resultBool = noteService.listNotes.isEmpty()

        assertFalse(resultBool)
        assertEquals(result, 1)
    }

    @Test
    fun createComment_shouldCreateComment() {
        val noteID = 1
        val message = "Тестовое сообщение"
        val title = "Hello, world"
        val text = "Тестовый текст"
        val noteService = NoteService()

        noteService.add(title, text)
        val result = noteService.createComment(noteID, message)
        val resultBool = noteService.listNotes[0].comments.isNotEmpty()

        assertTrue(resultBool)
        assertEquals(result, 1)
    }

    @Test(expected = NoteNotFoundException::class)
    fun createComment_shouldThrow() {
        val noteID = 2
        val message = "Тестовое сообщение"
        val title = "Hello, world"
        val text = "Тестовый текст"
        val noteService = NoteService()

        noteService.add(title, text)
        noteService.createComment(noteID, message)
    }

    @Test
    fun delete_shouldDeleteNote() {
        val noteID = 1
        val title = "Hello, world"
        val text = "Тестовый текст"
        val noteService = NoteService()

        noteService.add(title, text)
        val result = noteService.delete(noteID)
        val resultBool = noteService.listNotes.isEmpty()

        assertTrue(resultBool)
        assertTrue(result)
    }

    @Test(expected = NoteNotFoundException::class)
    fun delete_shouldThrow() {
        val noteID = 2
        val title = "Hello, world"
        val text = "Тестовый текст"
        val noteService = NoteService()

        noteService.add(title, text)
        noteService.delete(noteID)
    }

    @Test
    fun deleteComment_shouldDeleteComment() {
        val noteID = 1
        val commentID = 1
        val title = "Hello, world"
        val text = "Тестовый текст"
        val message = "Тестовое сообщение"
        val noteService = NoteService()

        noteService.add(title, text)
        noteService.createComment(noteID, message)
        val result = noteService.deleteComment(commentID)
        val resultBool = noteService.listNotes[0].comments[0].isDelete

        assertTrue(resultBool)
        assertTrue(result)
    }

    @Test(expected = CommentNotFoundException::class)
    fun deleteComment_shouldThrow() {
        val commentID = 1
        val title = "Hello, world"
        val text = "Тестовый текст"
        val noteService = NoteService()

        noteService.add(title, text)
        noteService.deleteComment(commentID)
    }

    @Test
    fun edit_shouldEditNote() {
        val noteID = 1
        val title = "Hello, world"
        val text = "Тестовый текст"
        val noteService = NoteService()
        val titleEdit = "Goodbye, world"
        val textEdit = "Текст"

        noteService.add(title, text)
        val result = noteService.edit(noteID, titleEdit, textEdit)

        assertNotEquals(title, noteService.listNotes[0].title)
        assertTrue(result)
    }

    @Test(expected = NoteNotFoundException::class)
    fun edit_shouldThrow() {
        val noteID = 2
        val title = "Hello, world"
        val text = "Тестовый текст"
        val noteService = NoteService()
        val titleEdit = "Goodbye, world"
        val textEdit = "Текст"

        noteService.add(title, text)
        noteService.edit(noteID, titleEdit, textEdit)
    }

    @Test
    fun editComment_shouldEditComment() {
        val commentID = 1
        val message = "Тестовое сообщение"
        val title = "Hello, world"
        val text = "Тестовый текст"
        val messageEdit = "Измененое сообщение"
        val noteService = NoteService()

        noteService.add(title, text)
        noteService.createComment(noteID=1, message)
        val result = noteService.editComment(commentID, messageEdit)

        assertNotEquals(message, noteService.listNotes[0].comments[0].message)
        assertTrue(result)
    }

    @Test(expected = CommentNotFoundException::class)
    fun editComment_shouldThrow() {
        val commentID = 1
        val title = "Hello, world"
        val text = "Тестовый текст"
        val messageEdit = "Измененое сообщение"
        val noteService = NoteService()

        noteService.add(title, text)
        noteService.editComment(commentID, messageEdit)
    }

    @Test
    fun get_shouldReturnListNotes() {
        val noteService = NoteService()
        noteService.add(title = "Заголовок 1 заметки", text = "Текст 1 заметки")
        Thread.sleep(1_000)
        noteService.add(title = "Заголовок 2 заметки", text = "Текст 2 заметки")
        Thread.sleep(1_000)
        noteService.add(title = "Заголовок 3 заметки", text = "Текст 3 заметки")
        Thread.sleep(1_000)
        noteService.add(title = "Заголовок 4 заметки", text = "Текст 4 заметки")


        val resultOffset = noteService.get(listOf(1, 2, 3, 4), 1)
        val resultCount = noteService.get(listOf(1, 2, 3, 4), count = 2)
        val resultSortZero = noteService.get(listOf(1, 2, 3, 4), sort = 0)
        val resultSortOne = noteService.get(listOf(1, 2, 3, 4), sort = 1)

        assertEquals(resultOffset, listOf(noteService.listNotes[1], noteService.listNotes[2], noteService.listNotes[3]))
        assertEquals(resultCount, listOf(noteService.listNotes[0], noteService.listNotes[1]))
        assertEquals(resultSortZero, listOf(noteService.listNotes[0], noteService.listNotes[1], noteService.listNotes[2], noteService.listNotes[3]))
        assertEquals(resultSortOne, listOf(noteService.listNotes[3], noteService.listNotes[2], noteService.listNotes[1], noteService.listNotes[0]))
    }

    @Test(expected = SortException::class)
    fun get_shouldThrow() {
        val noteService = NoteService()
        noteService.add(title = "Заголовок 1 заметки", text = "Текст 1 заметки")
        noteService.add(title = "Заголовок 2 заметки", text = "Текст 2 заметки")
        noteService.add(title = "Заголовок 3 заметки", text = "Текст 3 заметки")
        noteService.add(title = "Заголовок 4 заметки", text = "Текст 4 заметки")

        noteService.get(listOf(1, 2, 3, 4), sort = 2)
    }

    @Test
    fun getById() {
        val noteService = NoteService()

        val resultNull = noteService.getById(1)
        noteService.add("Title", "Text")
        val result = noteService.getById(1)

        assertEquals(resultNull, null)
        assertEquals(result, 1)
        assertNotEquals(result, 2)
    }

    @Test
    fun getComments_shouldReturnListComments() {
        val noteService = NoteService()
        noteService.add("title", "text")
        noteService.createComment(1, "message1")
        Thread.sleep(1_000)
        noteService.createComment(1, "message2")
        Thread.sleep(1_000)
        noteService.createComment(1, "message3")
        Thread.sleep(1_000)
        noteService.createComment(1, "message4")

        val resultOffset = noteService.getComments(1, offset = 2)
        val resultCount = noteService.getComments(1, count = 2)
        val resultSortZero = noteService.getComments(1, sort = 0)
        val resultSortOne = noteService.getComments(1, sort = 1)

        assertEquals(resultOffset?.size, 2)
        assertEquals(resultCount?.size, 2)
        assertEquals(resultSortZero, listOf(noteService.listNotes[0].comments[0], noteService.listNotes[0].comments[1], noteService.listNotes[0].comments[2], noteService.listNotes[0].comments[3]))
        assertEquals(resultSortOne, listOf(noteService.listNotes[0].comments[3], noteService.listNotes[0].comments[2], noteService.listNotes[0].comments[1], noteService.listNotes[0].comments[0]))
    }

    @Test(expected = SortException::class)
    fun getComments_shouldThrow() {
        val noteService = NoteService()
        noteService.add("title", "text")
        noteService.createComment(1, "message1")
        noteService.createComment(1, "message2")

        noteService.getComments(1, sort = 2)
    }

    @Test
    fun restoreComment() {
        val noteService = NoteService()
        noteService.add("title", "text")
        noteService.createComment(1, "message1")
        noteService.deleteComment(1)

        noteService.restoreComment(1)

        assertFalse(noteService.listNotes[0].comments[0].isDelete)
    }
}