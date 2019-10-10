package com.projet.azzed.androidvolley.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.projet.azzed.androidvolley.R

class DetailFragment : Fragment() {

    lateinit var webView: WebView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.detail_fragment, container, false)
        webView = root.findViewById(R.id.mywebview)
        val url = arguments?.getString("link")
        webView.loadUrl(url)
        return root
    }

}
