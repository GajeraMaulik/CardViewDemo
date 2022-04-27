package com.example.cardviewdemo.services.model

class ChatList {
    private var id: String? = null
    private var channelid:String? = null
    private var timestamp: Long? = null
    private  var lastmsg:String?=null
    private  var username:String?=null
    var seen:Boolean = false

    constructor()

    constructor(id: String?,channelid:String?, timestamp: Long?,lastmsg:String?,username:String?,seen:Boolean) {
        this.id = id
        this.channelid=channelid
        this.timestamp = timestamp
        this.lastmsg=lastmsg
        this.username=username
        this.seen = seen
    }
    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getTimestamp(): Long? {
        return timestamp
    }

    fun setTimestamp(timestamp: Long?) {
        this.timestamp = timestamp
    }
    fun getchannelid():String?{
        return channelid
    }
    fun setchannelid(channelid: String?) {
        this.channelid = channelid
    }

    fun  getlastmsg():String?{
        return lastmsg
    }
    fun setLastmsg(lastmsg: String?){
        this.lastmsg = lastmsg
    }
    fun getUsename(): String? {
        return username
    }
    fun setUsername(username: String?) {
        this.username = username
    }


    @JvmName("getSeen1")
    fun getSeen(): Boolean{
        return seen
    }

    @JvmName("setSeen1")
    fun setSeen(seen: Boolean) {
        this.seen = seen
    }




}