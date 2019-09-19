package com.example.azzed.androidvolley

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.azzed.androidvolley.utils.LogsUtils.longLog
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class RepoActivity : AppCompatActivity() {

    private var mRequestQueue: RequestQueue? = null
    var arrayRepos = arrayListOf<RepoModel>()
    lateinit var repoAdapter:RepoAdapter
    var langage:String? = null
    var frequency:String? = null

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repoAdapter = RepoAdapter(this)
        mRequestQueue = Volley.newRequestQueue(this)

        langage = intent.getStringExtra("language")
        frequency = intent.getStringExtra("frequency")

        Log.i("tryhard",langage + frequency)
        fetchJsonResponse()




    }

    private fun fetchJsonResponse() {
        val req = JsonArrayRequest(Request.Method.GET, "https://github-trending-api.now.sh/repositories?language=$langage&since=$frequency", null,
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
        mRequestQueue?.add(req)
    }

    fun populateRecycler(array: List<RepoModel>){
        repoAdapter.addAll(array)
        repoAdapter.notifyDataSetChanged()
        recylcerRepo.adapter = repoAdapter
        recylcerRepo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }


}

