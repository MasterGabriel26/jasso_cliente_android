package com.example.moveeventossaltillo.Adapters
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.telecom.VideoProfile.isVideo
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.annotation.OptIn
import androidx.cardview.widget.CardView
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView

import com.example.moveeventossaltillo.Models.Publicaciones
import com.example.moveeventossaltillo.R
import com.squareup.picasso.Picasso
class AdapterImagenVideoVerPublicacion(
    private val listaImagenes: ArrayList<String>,
    val context: Context,
    val mPublicaciones: Publicaciones
) : RecyclerView.Adapter<AdapterImagenVideoVerPublicacion.ViewHolder>() {


    @OptIn(UnstableApi::class) override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val url = listaImagenes[position]
        Log.d("urlVideoImagen",url)
        val imageView = holder.itemView.findViewById<ImageView>(R.id.imageView)
        val btnMute = holder.itemView.findViewById<ImageView>(R.id.btnMute)
        val playerView = holder.itemView.findViewById<PlayerView>(R.id.playerView)
        val cardViewItem = holder.itemView.findViewById<CardView>(R.id.cardViewItem)
        val isVideo = url.contains("mp4", ignoreCase = true)
        if (isVideo) {
            btnMute.visibility=View.VISIBLE
            playerView.setBackgroundColor(Color.BLACK)
            val mPlayer=ExoPlayer.Builder(context!!).build()
            mPlayer.volume = 0f
            playerView.player=mPlayer
            val mediaItem = MediaItem.Builder()
                .setUri(url)
                .setMimeType(MimeTypes.APPLICATION_MP4)
                .build()
            val mediaSource= ProgressiveMediaSource.Factory(
                DefaultDataSource.Factory(context)
            ).createMediaSource(mediaItem)
            mPlayer.setMediaItem(mediaItem)
            mPlayer.prepare()
            //mPlayer.play()
            imageView.visibility = View.GONE
            playerView.visibility = View.VISIBLE
            playerView.setOnClickListener {
                if (getNombreActivity(context) != "DetailProveedorPostsActivity") {
                    if (noHayImagenes()){
                        Log.d("cclcls","sda")
//                        val intent = Intent(context, DetailProveedorPostsActivity::class.java)
//                        intent.putExtra("publicaciones", mPublicaciones)
//                        context?.startActivity(intent)
                    }

                }

            }

        } else {
            imageView.setOnClickListener {
                if (getNombreActivity(context) != "DetailProveedorPostsActivity") {
                    Log.d("cclcls","sda")
//                    val intent = Intent(context, DetailProveedorPostsActivity::class.java)
//                    intent.putExtra("publicaciones", mPublicaciones)
//                    context?.startActivity(intent)
                }

            }
            btnMute.visibility=View.GONE
            imageView.visibility = View.VISIBLE
            playerView.visibility = View.GONE
            Picasso.get().load(url).into(imageView)
        }

    }

    private fun noHayImagenes(): Boolean {
        return listaImagenes.none { !it.contains("mp4", ignoreCase = true) }
    }
    private fun getNombreActivity(context: Context?): String {
        var nombre = context.toString()
        var hasta: Int = 0
        var desde: Int = 0
        for (i in nombre.indices) {
            if (nombre[i].equals('@')) {
                hasta = i
            }
        }
        for (i in nombre.indices) {
            if (nombre[i].equals('.')) {
                desde = i + 1
            }
        }
        return nombre.substring(desde, hasta)
    }
    init {
        sortListByVideo()
    }
    private fun sortListByVideo() {
        listaImagenes.sortWith(compareByDescending { it.contains("mp4", ignoreCase = true) })
    }
    public class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            Log.d("ViewHolder", "Inicializando ViewHolder")
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_video_image, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return listaImagenes?.size ?: 0
    }
}