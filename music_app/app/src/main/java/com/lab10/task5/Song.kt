package com.lab10.task5

class Song() {
    lateinit var singer: String
    lateinit var song: String
    var playsNumber:Int = 0
    var userListenedNum :Int = 0
    constructor(singer: String, song: String, playsNumber: Int, userListenedNum: Int):this() {
        this.singer = singer
        this.song = song
        this.playsNumber = playsNumber
        this.userListenedNum = userListenedNum
    }
    constructor(singer: String):this() {
       this.singer = singer
    }
}