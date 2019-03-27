package com.example.azzed.androidvolley

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.azzed.androidvolley.utils.BaseListAdapter
import com.example.azzed.androidvolley.utils.ImageUtils
import kotlinx.android.synthetic.main.view_repo.view.*
import kotlin.math.acos


class RepoAdapter(repoActivity: RepoActivity) : BaseListAdapter<RepoModel>(){
    var myactivity = repoActivity
    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_repo, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindChildViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repo = items[position]
        holder.itemView.apply {
            tvName.text = repo.name
            tvDescription.text = repo.description
            tvLogin.text = repo.login
            tvRating.text = withSuffix(repo.stars.toLong())

            if (!repo.avatar.isEmpty())
                ImageUtils.loadImage(repo.avatar, R.mipmap.noimage, picture)
            else
                picture.setBackgroundResource(R.mipmap.noimage)
        }

        holder.itemView.setOnClickListener {
            val url = repo.html_url

            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            myactivity.startActivity(i)
        }
    }
}