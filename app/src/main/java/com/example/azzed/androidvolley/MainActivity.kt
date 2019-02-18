package com.example.azzed.androidvolley

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject




class MainActivity : AppCompatActivity() {

    private var mRequestQueue: RequestQueue? = null

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRequestQueue = Volley.newRequestQueue(this);
        fetchJsonResponse()

    }



    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun fetchJsonResponse() {
        // Pass second argument as "null" for GET requests
        val req = JsonObjectRequest(Request.Method.GET, "https://api.github.com/search/repositories?q=created:%3E2017-10-22&sort=stars&order=desc&page", null,
                Response.Listener
                { response ->
                    try {
                        println(response)
                        val jsonArray = response.getJSONArray("items")
                        for (i in 0 until jsonArray.length()) {

                            val jo = jsonArray.getJSONObject(i)
                            val owner = jo.getJSONObject("owner")
                            val avatar = owner.getString("avatar_url")
                            val login = owner.getString("login")
                            val name = jo.getString("name")
                            val description = jo.getString("description")
                            val stars = jo.getString("stargazers_count")

                            val repos = GithubRepository()
                            repos.avatar = avatar
                            repos.login = login
                            repos.name = name
                            repos.description = description
                            repos.stars = stars
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },

                Response.ErrorListener { error ->
                    Log.i("tryhard1", error.localizedMessage + error.message)
                }
        )


        /* Add your Requests to the RequestQueue to execute */
        mRequestQueue?.add(req)

    }


}

