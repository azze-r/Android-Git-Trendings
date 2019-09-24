package com.projet.azzed.androidvolley.model

class RepoModel {
    var avatar =""
    var login =""
    var name =""
    var description =""
    var stars =""
    var html_url = ""
    override fun toString(): String {
        return "GithubRepository(avatar='$avatar', login='$login', name='$name', description='$description', stars='$stars')"
    }

}