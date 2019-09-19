package com.example.azzed.androidvolley

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.azzed.androidvolley.utils.LogsUtils
import com.example.azzed.androidvolley.utils.LogsUtils.longLog
import kotlinx.android.synthetic.main.activity_configure_trends.*
import org.json.JSONException
import java.util.*
import android.widget.ArrayAdapter
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Toast


class ConfigureTrendsActivity : AppCompatActivity() {

    private var mRequestQueue: RequestQueue? = null
    var link = "https://github-trending-api.now.sh/languages"
    var arrayRepos = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configure_trends)

        mRequestQueue = Volley.newRequestQueue(this)
        fetchJsonResponse()

        val arrayFrequency = arrayListOf<String>()
        arrayFrequency.add("daily")
        arrayFrequency.add("weekly")
        arrayFrequency.add("monthly")

        val adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, arrayFrequency)
        frequencyAutoComplete.setAdapter(adapter)

        button.setOnClickListener {
            if (langageAutoComplete.text.isEmpty() or frequencyAutoComplete.text.isEmpty()) {
                Toast.makeText(this, "Please choice language and frequency", Toast.LENGTH_LONG).show()
            }
            else {
                val intent = Intent(this, RepoActivity::class.java)
                intent.putExtra("language", langageAutoComplete.text.toString())
                intent.putExtra("frequency", frequencyAutoComplete.text.toString())
                startActivity(intent)
            }
        }

    }

    private fun fetchJsonResponse() {
        val req = JsonArrayRequest(Request.Method.GET, link, null,
                Response.Listener
                { response ->
                    try {
                        for (i in 0 until response.length()) {
                            val jo = response.getJSONObject(i)
                            arrayRepos.add(jo.getString("urlParam"))
                        }

                        val adapter = ArrayAdapter<String>(this,
                                android.R.layout.simple_dropdown_item_1line, arrayRepos)
                        langageAutoComplete.setAdapter(adapter)
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

}
