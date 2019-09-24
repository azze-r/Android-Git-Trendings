package com.projet.azzed.androidvolley.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.projet.azzed.androidvolley.R
import com.projet.azzed.androidvolley.model.RepoModel
import com.projet.azzed.androidvolley.ui.ReposFragment
import com.projet.azzed.androidvolley.utils.BaseListAdapter
import com.projet.azzed.androidvolley.utils.ImageUtils
import kotlinx.android.synthetic.main.view_repo.view.*


class RepoAdapter(reposFragment: ReposFragment) : BaseListAdapter<RepoModel>(){
    var myactivity = reposFragment
    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_repo, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindChildViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repo = items[position]
        holder.itemView.apply {
            tvLogin.text = repo.login
            tvName.text = repo.name
            ImageUtils.loadImage(repo.avatar, R.mipmap.noimage, picture)
            tvDescription.text = repo.description
            tvRating.text = withSuffix(repo.stars.toLong())
        }

        holder.itemView.setOnClickListener {
            val url = repo.html_url

            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            myactivity.startActivity(i)
        }
    }
}