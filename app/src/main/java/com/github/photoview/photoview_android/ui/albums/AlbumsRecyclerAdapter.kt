package com.github.photoview.photoview_android.ui.albums

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.github.photoview.photoview_android.MyAlbumsQuery
import com.github.photoview.photoview_android.R
import com.github.photoview.photoview_android.appSharedPreferences
import com.github.photoview.photoview_android.fragment.AlbumItem
import com.github.photoview.photoview_android.protectedMediaDownloader
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.net.URL

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

class AlbumsRecyclerAdapter(private val albums: List<MyAlbumsQuery.MyAlbum>) : RecyclerView.Adapter<AlbumsRecyclerAdapter.AlbumHolder>() {

    class AlbumHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var album: AlbumItem? = null

        private val imageView: ImageView

        init {
            v.setOnClickListener(this)
            this.imageView = v.findViewById(R.id.item_image)
        }

        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
        }

        fun bindAlbum(album: AlbumItem) {
            this.album = album
            val instanceUrl = view.context.appSharedPreferences().getInstanceUrl()!!

            try {
                val picasso = Picasso.Builder(view.context).downloader(protectedMediaDownloader(view.context)).build()
                picasso.load(instanceUrl + album.thumbnail?.thumbnail?.url).resize(1024, 1024).centerCrop().into(imageView);
            } catch (e: Exception) {
                Log.w("RecyclerView", "Failed to download album thumbnail: ${e.message}")
            }

        }

        companion object {
            private val ALBUM_KEY = "ALBUM"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_album_item, false)
        return AlbumHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: AlbumHolder, position: Int) {
        holder.bindAlbum(albums[position].albumItem)
    }

    override fun getItemCount(): Int = albums.size
}