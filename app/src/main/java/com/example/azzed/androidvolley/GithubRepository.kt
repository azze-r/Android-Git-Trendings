package com.example.azzed.androidvolley

class GithubRepository {
    var avatar =""
    var login =""
    var name =""
    var description =""
    var stars =""

    override fun toString(): String {
        return "GithubRepository(avatar='$avatar', login='$login', name='$name', description='$description', stars='$stars')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GithubRepository

        if (avatar != other.avatar) return false
        if (login != other.login) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (stars != other.stars) return false

        return true
    }

    override fun hashCode(): Int {
        var result = avatar.hashCode()
        result = 31 * result + login.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + stars.hashCode()
        return result
    }

}