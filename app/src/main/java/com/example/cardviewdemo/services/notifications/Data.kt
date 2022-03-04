package com.example.cardviewdemo.services.notifications

class Data(toString: String, s1: String, s2: String, userId_receiver: String) {
    private var user: String? = null
    private var icon: String? = null
    private var body: String? = null
    private var title: String? = null
    private var sent: String? = null

    fun Data() {}

    fun Data(user: String?, body: String?, title: String?, sent: String?) {
        this.user = user
        this.icon = icon
        this.body = body
        this.title = title
        this.sent = sent
    }

    fun getUser(): String? {
        return user
    }

    fun setUser(user: String?) {
        this.user = user
    }

    fun getIcon(): String? {
        return icon
    }

    fun setIcon(icon: String?) {
        this.icon = icon
    }

    fun getBody(): String? {
        return body
    }

    fun setBody(body: String?) {
        this.body = body
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String?) {
        this.title = title
    }

    fun getSent(): String? {
        return sent
    }

    fun setSent(sent: String?) {
        this.sent = sent
    }


}