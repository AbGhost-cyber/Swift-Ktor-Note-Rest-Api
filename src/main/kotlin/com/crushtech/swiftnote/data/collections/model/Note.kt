package com.crushtech.swiftnote.data.collections.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Note(
    val title:String,
    val content:String,
    val date:Long,
    val owner:String,
    val color:String,
    var isPinned:Boolean= false,
    @BsonId
    val id:String = ObjectId().toString()

)