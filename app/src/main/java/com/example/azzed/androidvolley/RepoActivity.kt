package com.example.azzed.androidvolley

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException
import java.util.*






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

    }

    private fun fetchJsonResponse(page:Int) {
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

        Log.i("mydate","$year-$month-$day")
        Log.i("mydate","https://github-trending-api.now.sh/repositories?language=kotlin&since=monthly")
        // Pass second argument as "null" for GET requests
        val req = JsonArrayRequest(Request.Method.GET, "https://github-trending-api.now.sh/repositories?language=kotlin&since=monthly", null,
                Response.Listener
                { response ->
                    try {

                        for (i in 0 until response.length()) {
                            val jo = response.getJSONObject(i)

                            val repo = RepoModel()

                            repo.login = jo.getString("author")
                            repo.name = jo.getString("name")
                            repo.avatar = jo.getString("avatar")
                            repo.html_url = jo.getString("url")
                            repo.description = jo.getString("description")
                            repo.stars = jo.getString("stars")
                            Log.i("tryhard",jo.toString())
//                            val obj = jo.getJSONObject("repo")
//                            repo.name = obj.getString("name")
//                            repo.description = obj.getString("descriptio")
//                            repo.avatar = obj.getString("url")

                            arrayRepos.add(repo)
                        }
                        populateRecycler(arrayRepos)
                    }
                    catch (e: JSONException) {
                        e.printStackTrace()

                    }

                },

                Response.ErrorListener { error ->
                    longLog(error.toString())
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

    fun longLog(str: String) {
        if (str.length > 4000) {
            Log.d("tryhard", str.substring(0, 4000))
            longLog(str.substring(4000))
        } else
            Log.d("tryhard", str)
    }

}

