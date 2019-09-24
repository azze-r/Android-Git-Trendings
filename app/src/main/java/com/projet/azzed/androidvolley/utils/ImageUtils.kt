package com.projet.azzed.androidvolley.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.ImageView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.ByteArrayOutputStream

class ImageUtils {
    // Get an image from smartphone, and convert it in 64 bits, to send it to GraphQl in 64 format
    // and "suppose" to check if it's > 1mb

    companion object {

        fun loadImageBase(image: String?, holderResId: Int, imageView: ImageView) {
            image?.let {
                if (!it.isNullOrEmpty()){
                    val bitmap = convert(it)
                    Glide.with(imageView).load(bitmap).apply(RequestOptions().placeholder(holderResId).error(holderResId)).into(imageView)
            }}
        }

        fun loadImage(imageRef: String?, holderResId: Int, imageView: ImageView) {
            Glide.with(imageView).load(imageRef).apply(RequestOptions().placeholder(holderResId)).into(imageView)
        }

        fun loadImage(uri: Uri?, holderResId: Int, imageView: ImageView) {
            Glide.with(imageView).load(uri).apply(RequestOptions().placeholder(holderResId)).into(imageView)
        }

        fun loadRoundImage(imageRef: String?, holderResId: Int, imageView: ImageView) {
            Glide.with(imageView).load(imageRef).apply(RequestOptions().circleCrop().placeholder(holderResId)).into(imageView)
        }

        fun loadDiapo(url: String?, imageView: ImageView, transitionId: Int) {
            Glide.with(imageView)
                    .load(url)
                    .transition(GenericTransitionOptions.with(transitionId))
                    .apply(RequestOptions()
                            .centerCrop())
                    .into(imageView)
        }

        @Throws(IllegalArgumentException::class)
        fun convert(base64Str: String?): Bitmap? {
            val decodedBytes = Base64.decode(
                    base64Str?.substring(base64Str.indexOf(",") + 1),
                    Base64.DEFAULT
            )
            return if (decodedBytes.isNotEmpty())
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            else
                null
        }

        fun convert(bitmap: Bitmap): String {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        }

    }

}