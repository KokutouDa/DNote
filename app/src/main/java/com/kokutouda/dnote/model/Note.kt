package com.kokutouda.dnote.model

/**
 * Created by apple on 2017/7/18.
 */
class Note(var map: MutableMap<String, Any?>) {
    var _id: Long by map
    var title: String by map
    var content: String by map
    var date: Long by map

    constructor(title: String, content: String, date: Long) : this(HashMap()) {
        this.title = title
        this.content = content
        this.date = date
    }
}