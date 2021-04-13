package com.crushtech.swiftnote.data

import com.crushtech.swiftnote.data.collections.model.Note
import com.crushtech.swiftnote.data.collections.model.User
import com.crushtech.swiftnote.security.checkHashForPassword
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("NoteDatabase")
private val users = database.getCollection<User>()
private val notes = database.getCollection<Note>()

//register user
suspend fun registerUser(user: User): Boolean {
    return users.insertOne(user).wasAcknowledged()
}

//check if a user exists by email
suspend fun checkIfUserExistsByEmail(email: String): Boolean {
    return users.findOne(User::email eq email) != null
}

suspend fun checkIfUserExistsById(id: String): Boolean {
    return users.findOne(User::uid eq id) != null
}

//check password
suspend fun checkPasswordForUser(email: String, passwordToCheck: String): Boolean {
    val actualPassword = users.findOne(User::email eq email)?.password ?: return false
    return checkHashForPassword(passwordToCheck, actualPassword)
}

//get specific user
suspend fun getUser(id: String): User {
    return users.findOneById(id)!!
}

//delete user
suspend fun deleteUser(id: String): Boolean {
    return users.deleteOne(User::uid eq id).wasAcknowledged()
}

//get user's username via email
suspend fun getUsernameFromEmail(email: String): String {
    return users.findOne(User::email eq email)!!.username
}

//save note to our mongo server
suspend fun saveNote(note: Note): Boolean {
    //check if note exists
    val noteExists = notes.findOneById(note.id) != null
    return if (noteExists) {
        //update note
        notes.updateOneById(note.id, note).wasAcknowledged()
    } else {
        //insert if note doesn't exist
        notes.insertOne(note).wasAcknowledged()
    }
}

//delete note by id
suspend fun deleteNote(noteId: String): Boolean {
    return notes.deleteOneById(noteId).wasAcknowledged()
}

//get list of user's note
suspend fun getUsersNote(email: String): List<Note> {
    return notes.find(Note::owner eq email).toList()
}

//get specific note
suspend fun getNoteById(noteId: String): Note {
    return notes.findOneById(noteId)!!
}
suspend fun checkIfNoteExists(id: String): Boolean {
    return notes.findOne(Note::id eq id) != null
}