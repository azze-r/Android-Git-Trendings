package com.example.azzed.androidvolley

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import java.util.*
import android.widget.Toast
import android.support.v4.view.ViewCompat.canScrollVertically
import android.support.v7.widget.RecyclerView



class RepoActivity : AppCompatActivity() {

    private var mRequestQueue: RequestQueue? = null
    var arrayRepos = arrayListOf<RepoModel>()
    lateinit var repoAdapter:RepoAdapter
    var page = 0
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repoAdapter = RepoAdapter(this)

        mRequestQueue = Volley.newRequestQueue(this);
        fetchJsonResponse(page)
        println(Calendar.getInstance().toString())

        // when scroll is down, reload data with new page
        recylcerRepo.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    fetchJsonResponse(page)
                    page++
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun fetchJsonResponse(page:Int) {
        arrayRepos.clear()
        val date = Date() // your date
        val cal = Calendar.getInstance()
        cal.time = date
        val year = cal.get(Calendar.YEAR).toString()
        var month = cal.get(Calendar.MONTH).toString()
        // ISO 8601 date/time value, such as YYYY-MM-DD."
        if (month.length==1)
            month = "0$month"

        var day = cal.get(Calendar.DAY_OF_MONTH).toString()
        if (day.length==1)
            day = "0$day"

        Log.i("mydate","https://api.github.com/search/repositories?q=created:%3E$year-$month-$day&sort=stars&order=desc&page")
        // Pass second argument as "null" for GET requests
        val req = JsonObjectRequest(Request.Method.GET, "https://api.github.com/search/repositories?q=created:%3E$year-$month-$day&sort=stars&order=desc&page=$page", null,
                Response.Listener
                { response ->
                    try {
                        val jsonArray = response.getJSONArray("items")
                        println(jsonArray.length())

                        for (i in 0 until jsonArray.length()) {

                            val jo = jsonArray.getJSONObject(i)
                            val owner = jo.getJSONObject("owner")
                            val avatar = owner.getString("avatar_url")

                            val login = owner.getString("login")
                            val name = jo.getString("name")
                            val description = jo.getString("description")
                            val stars = jo.getString("stargazers_count")
                            val html_url = jo.getString("html_url")

                            val repo = RepoModel()
                            repo.avatar = avatar
                            repo.login = login
                            repo.name = name
                            repo.description = description
                            repo.stars = stars
                            repo.html_url = html_url
                            arrayRepos.add(repo)
                        }

                        populateRecycler(arrayRepos)

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

    fun populateRecycler(array: List<RepoModel>){
        repoAdapter.addAll(array)
        repoAdapter.notifyDataSetChanged()
        if (repoAdapter!=null) {
            recylcerRepo.adapter = repoAdapter
            recylcerRepo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }

    }



}

