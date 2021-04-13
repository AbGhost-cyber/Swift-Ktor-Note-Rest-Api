package com.crushtech.swiftnote.data.collections.model

//import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

//@Serializable
data class User(
    val email: String,
    val password: String,
    val username: String,
    @BsonId
    val uid: String = ObjectId().toString()
)
