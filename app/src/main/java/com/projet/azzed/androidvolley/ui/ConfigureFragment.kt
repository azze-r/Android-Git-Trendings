package com.projet.azzed.androidvolley.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.projet.azzed.androidvolley.R
import com.projet.azzed.androidvolley.utils.LogsUtils.longLog
import org.json.JSONException


class ConfigureFragment : Fragment() {

    private var mRequestQueue: RequestQueue? = null
    var link = "https://github-trending-api.now.sh/languages"
    var arrayRepos = arrayListOf<String>()
    lateinit var langageAutoComplete:androidx.appcompat.widget.AppCompatAutoCompleteTextView
    lateinit var frequencyAutoComplete:androidx.appcompat.widget.AppCompatAutoCompleteTextView
    lateinit var button:FloatingActionButton

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?

    ): View? {

        val root = inflater.inflate(R.layout.fragment_configure_trends, container, false)
        langageAutoComplete = root.findViewById(R.id.langageAutoComplete)
        frequencyAutoComplete = root.findViewById(R.id.frequencyAutoComplete)
        button = root.findViewById(R.id.button)

        var sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

        val defaultLangage = resources.getString(R.string.saved_langage)
        val langage = sharedPref?.getString(getString(R.string.saved_langage), defaultLangage)

        val defaultFrequency = resources.getString(R.string.saved_frequency)
        val frequency = sharedPref?.getString(getString(R.string.saved_frequency), defaultFrequency)

        langageAutoComplete.setText(langage)
        frequencyAutoComplete.setText(frequency)

        mRequestQueue = Volley.newRequestQueue(context)
        fetchJsonResponse()

        val arrayFrequency = arrayListOf<String>()
        arrayFrequency.add("daily")
        arrayFrequency.add("weekly")
        arrayFrequency.add("monthly")

        val adapter = context?.let {
            ArrayAdapter<String>(it,
                android.R.layout.simple_dropdown_item_1line, arrayFrequency)
        }
        frequencyAutoComplete.setAdapter(adapter)

        button.setOnClickListener {
            if (langageAutoComplete.text.isEmpty() or frequencyAutoComplete.text.isEmpty()) {
                Toast.makeText(context, "Please choice language and frequency", Toast.LENGTH_LONG).show()
            }
            else {
                sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)!!
                with (sharedPref!!.edit()) {
                    putString(getString(R.string.saved_langage), langageAutoComplete.text.toString())
                    putString(getString(R.string.saved_frequency), frequencyAutoComplete.text.toString())
                    commit()
                }

                val bundle = Bundle()
                bundle.putString("langage", langageAutoComplete.text.toString())
                bundle.putString("frequency", frequencyAutoComplete.text.toString())

                it.findNavController().navigate(R.id.action_navigation_diapo_to_navigation_repos,bundle)
            }
        }


        return root

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

                        val adapter = context?.let {
                            ArrayAdapter<String>(it,
                                    android.R.layout.simple_dropdown_item_1line, arrayRepos)
                        }
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
