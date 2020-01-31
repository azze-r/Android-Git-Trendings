package com.projet.azzed.androidvolley.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.projet.azzed.androidvolley.utils.LogsUtils.longLog
import org.json.JSONException
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.projet.azzed.androidvolley.MainActivity
import com.projet.azzed.androidvolley.R
import com.projet.azzed.androidvolley.adapters.RepoAdapter
import com.projet.azzed.androidvolley.model.RepoModel


class ReposFragment : Fragment() {

    private var mRequestQueue: RequestQueue? = null
    var arrayRepos = arrayListOf<RepoModel>()
    var langage:String? = null
    var frequency:String? = null
    lateinit var progress: ProgressBar
    lateinit var fab: FloatingActionButton
    lateinit var recylcerRepo:RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_repos, container, false)
        progress = root.findViewById(R.id.progress)
        fab = root.findViewById(R.id.myfab)
        recylcerRepo = root.findViewById(R.id.recylcerRepo)

        if (arguments?.getString("langage").isNullOrEmpty()){
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            val defaultLangage = resources.getString(R.string.saved_langage)
            langage = sharedPref?.getString(getString(R.string.saved_langage), defaultLangage)
            val defaultFrequency = resources.getString(R.string.saved_frequency)
            frequency = sharedPref?.getString(getString(R.string.saved_frequency), defaultFrequency)
        }
        else {
            langage = arguments?.getString("langage")
            frequency = arguments?.getString("frequency")
        }

        progress.visibility = View.VISIBLE
        mRequestQueue = Volley.newRequestQueue(context)

        fab.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigation_repos_to_navigation_configure)
        }

        Log.i("tryhard","on create view")
        fetchJsonResponse()

        return root
    }


    private fun fetchJsonResponse() {

        arrayRepos.clear()
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
                            arrayRepos.add(repo)
                        }
                        progress.visibility = View.GONE
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

    private fun populateRecycler(array: List<RepoModel>){
        val repoAdapter = RepoAdapter(this)
        Log.i("tryhard", array.size.toString())
        repoAdapter.addAll(array)
        repoAdapter.notifyDataSetChanged()
        recylcerRepo.adapter?.notifyDataSetChanged()
        recylcerRepo.adapter = repoAdapter
        recylcerRepo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)!!
        with (sharedPref.edit()) {
            putString(getString(R.string.saved_langage),langage)
            putString(getString(R.string.saved_frequency), frequency)
            commit()
        }
    }

}

