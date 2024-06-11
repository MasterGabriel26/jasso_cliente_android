package com.example.moveeventossaltillo.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.moveeventossaltillo.Models.Like
import com.example.moveeventossaltillo.Models.Publicaciones
import com.example.moveeventossaltillo.Provider.ComentarioProvider
import com.example.moveeventossaltillo.Provider.LikeProvider
import com.example.moveeventossaltillo.Provider.PublicacionesProvider
import com.example.moveeventossaltillo.Provider.UsuariosProvider
import com.example.moveeventossaltillo.R
import com.example.moveeventossaltillo.ui.home.HomeFragment
import com.example.moveeventossaltillo.utils.RelativeTime
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.Date
import java.util.Locale

class AdapterMisPublicaionesProveedor(
    options: FirestoreRecyclerOptions<Publicaciones?>?,
    var context: Context
) : FirestoreRecyclerAdapter<Publicaciones, AdapterMisPublicaionesProveedor.ViewHolder>(options!!) {

    //    private lateinit var proveedorPostList: ArrayList<Publicaciones>
    private var proveedorPostList: MutableList<Publicaciones> = mutableListOf()
    private var filteredList: MutableList<Publicaciones> = mutableListOf()

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mLikeProvider: LikeProvider
//    private lateinit var mProveedorHomeActivity: ProveedorHomeActivity
    private lateinit var mPublicacionesProvider: PublicacionesProvider
    var banderaClickLike = true
    var currentPosition = 0
    override fun onBindViewHolder(holder: ViewHolder, position: Int, mPublicaciones: Publicaciones) {
        Log.d("proveedorPostList", "proveedorPostList ${proveedorPostList}")
        mAuth = FirebaseAuth.getInstance()
        //val verDetalle = holder.itemView.findViewById<Button>(R.id.verDetalle)
        val titulo = holder.itemView.findViewById<TextView>(R.id.titulo)
        val nombrePost = holder.itemView.findViewById<TextView>(R.id.nombrePost)
        val costo = holder.itemView.findViewById<TextView>(R.id.costo)
        val fecha = holder.itemView.findViewById<TextView>(R.id.fecha)
        val tipoEvento = holder.itemView.findViewById<TextView>(R.id.tipoEvento)
        val rv_video_image = holder.itemView.findViewById<RecyclerView>(R.id.rv_video_image)
        val imageLike = holder.itemView.findViewById<ImageView>(R.id.imageLike)
        val Nmegusta = holder.itemView.findViewById<TextView>(R.id.Nmegusta)
        val imagenComment = holder.itemView.findViewById<ImageView>(R.id.imagenComment)
        val NComentarios = holder.itemView.findViewById<TextView>(R.id.NComentarios)
        val precioDelPaquete = holder.itemView.findViewById<TextView>(R.id.precioCardHome)
        val flechaDerecha = holder.itemView.findViewById<ImageView>(R.id.imageView17)
        val flechaIzquierda = holder.itemView.findViewById<ImageView>(R.id.imageView11)
//        com.google.android.material.imageview.ShapeableImageView
        val iv_proveedor = holder.itemView.findViewById<ShapeableImageView>(R.id.iv_proveedor)
        val tv_proveedor = holder.itemView.findViewById<TextView>(R.id.tv_proveedor)
        //val CantidadPersonas = holder.itemView.findViewById<TextView>(R.id.tv_CantidadPersonas)
        val eventos = "${mPublicaciones.tipoEvento!!.joinToString(", ")}"
        val relative = RelativeTime.RelativeTime.getDateTime(mPublicaciones.fechaPost)
        val uris = mPublicaciones.multimediaUrl
        val constrain = holder.itemView.findViewById<ConstraintLayout>(R.id.constrainInfoCardHome)

        tipoEvento.text = eventos
        costo.text = mPublicaciones.costoPaquete
        titulo.text = mPublicaciones.tituloEvento
        nombrePost.text = mPublicaciones.categoria
        val precioDividido = mPublicaciones.costoPaquete.toDouble() // Convertir a Double si es necesario
        val format = NumberFormat.getNumberInstance(Locale.getDefault())
        val precioFormateado = format.format(precioDividido)
        precioDelPaquete.text = "$${precioFormateado}"


        val uidUsuario = mPublicaciones.uid
        val mUsuariosProvider = UsuariosProvider()

        iv_proveedor.setOnClickListener {
            // Suponiendo que tienes una actividad llamada `UserProfileActivity` y que el uid del usuario es lo que necesitas para cargar su perfil.
//            val intent = Intent(context, ProfileProveedorActivity::class.java)
//            intent.putExtra(
//                "uid",
//                mPublicaciones.uid
//            ) // Envía el uid del usuario a la actividad de perfil.
//            context?.startActivity(intent)
        }
        mUsuariosProvider.getUsuario(uidUsuario).addOnSuccessListener { documento ->
            if (documento.exists()) {
                val nombre = documento.getString("name") ?: "Nombre no disponible"
                val imagenPerfil = documento.getString("imageProfile")

                tv_proveedor.text = nombre

                if (!imagenPerfil.isNullOrEmpty()) {
                    Glide.with(iv_proveedor.context)
                        .load(imagenPerfil)
                        .placeholder(R.drawable.move_logo) // Imagen por defecto
                        .error(R.drawable.move_logo) // Imagen en caso de error
                        .into(iv_proveedor)
                } else {
                    iv_proveedor.setImageResource(R.drawable.move_logo) // Imagen por defecto si no hay URL
                }
            }
        }.addOnFailureListener {
            Log.e("getUsuarioById", "Error al obtener los datos del usuario", it)
        }

        Log.d("sadggfgdg6", "llll")
        //CantidadPersonas.text = mPublicaciones.cantidadDePersonas
//        if (getNombreActivity(context) == "MisPublicacionesProveedorActivity") {
//            imMenu.visibility = View.VISIBLE
//
//        } else {
//            imMenu.visibility = View.GONE
//
////        }
//        getUsuarioAdmin(imMenu)
        showRecyclerImageVideo(uris, rv_video_image, mPublicaciones)
        flechaIzquierda.setOnClickListener {
            val newPosition = currentPosition - 1
            if (newPosition >= 0) {
                currentPosition = newPosition
                rv_video_image.smoothScrollToPosition(currentPosition)
            }
        }
        flechaDerecha.setOnClickListener {
            val newPosition = currentPosition + 1
            if (newPosition < mPublicaciones.multimediaUrl!!.size) {
                currentPosition = newPosition
                rv_video_image.smoothScrollToPosition(currentPosition)
            }
        }
        constrain.setOnClickListener {
            if (getNombreActivity(context) != "DetailProveedorPostsActivity") {
//                Log.d("cclcls", "sda")
//                val intent = Intent(context, DetailProveedorPostsActivity::class.java)
//                intent.putExtra("publicaciones", mPublicaciones)
//                context?.startActivity(intent)
            }

        }
        if (mPublicaciones.multimediaUrl?.size == 1) {
            flechaDerecha.visibility = View.GONE
            flechaIzquierda.visibility = View.GONE
        }

        getUsuarioById(iv_proveedor, tv_proveedor, mPublicaciones.uid)
        clickLike(imageLike, mPublicaciones, imagenComment)
        getNmeGustabypost(mPublicaciones.id, Nmegusta)
        getNmeComentariosbypost(mPublicaciones.id, NComentarios)
        SiExistelike(mPublicaciones.id, mAuth.uid.toString(), imageLike)
    }


    fun getUsuarioById(iv_proveedor: ShapeableImageView, tv_proveedor: TextView, uid: String) {
        val mUsuariosProvider = UsuariosProvider()
        mUsuariosProvider.getUsuario(uid).addOnSuccessListener { it ->
            if (it.exists()) {
                if (it.contains("name")) {
                    tv_proveedor.text = it?.getString("name").toString()
                }
                if (it.contains("imageProfile")) {
                    val imagen = it?.getString("imageProfile").toString()
                    if (imagen.isNotEmpty()) {
                        Picasso.get().load(imagen).into(iv_proveedor)
                    }
                }
            }

        }
    }

    private fun showRecyclerImageVideo(urls: ArrayList<String>?, rvVideoImage: RecyclerView?, mPublicaciones: Publicaciones) {
        val adapter = AdapterImagenVideoVerPublicacion(urls!!, context!!, mPublicaciones)
        rvVideoImage!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvVideoImage!!.adapter = adapter
        //vamos a ver si funciona las flechas
        rvVideoImage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val currentPosition = (recyclerView.layoutManager as LinearLayoutManager)
                    .findFirstVisibleItemPosition()
            }
        })
    }
    private fun getNmeComentariosbypost(idPost: String, NComentarios: TextView) {
        val mComentarioProvider = ComentarioProvider()
        mComentarioProvider.getComentariobyPost(idPost).addSnapshotListener { it, error ->
            val N: Int? = it?.size()
            NComentarios.text = "${N}"
        }
    }
    private fun getNmeGustabypost(idPost: String, Nmegusta: TextView) {
        mLikeProvider = LikeProvider()
        mLikeProvider.getLikestbyPost(idPost)
            .addSnapshotListener { it, error ->
                val N: Int? = it?.size()
                Nmegusta.text = "${N}"
            }
    }
    private fun clickLike(imageLike: ImageView, mPublicaciones: Publicaciones, imagenComment: ImageView) {
        imageLike.setOnClickListener {
            if (banderaClickLike) {
                banderaClickLike = !false
                val mlike = Like()
                mlike.uid = mAuth.uid.toString()
                mlike.idPost = mPublicaciones.id
                mlike.fecha = Date().time
                like(mlike, imageLike)
            }
        }
        imagenComment.setOnClickListener {
//            mProveedorHomeActivity = ProveedorHomeActivity()
//            mProveedorHomeActivity.verPostListener(View(context), mPublicaciones)
        }
    }
    private fun like(like: Like, imageLike: ImageView) {
        mAuth = FirebaseAuth.getInstance()
        mLikeProvider = LikeProvider()
        mLikeProvider.getLikeByUserAndPost(mAuth.uid.toString(), like.idPost).get()
            .addOnSuccessListener {
                val numberDocuments: Int = it.size()
                if (numberDocuments > 0) {
                    val idLike: String = it.documents[0].id
                    mLikeProvider.deleteLike(idLike)
                    imageLike.setImageResource(R.drawable.baseline_favorite_border_24)
                    banderaClickLike = true
                    actualizarNLikes(like.idPost, false)
                } else {
                    mLikeProvider.nuevoLike(like)
                    imageLike.setImageResource(R.drawable.baseline_favorite_24)
                    banderaClickLike = true
                    actualizarNLikes(like.idPost, true)
                }
            }
    }
    private fun SiExistelike(idPost: String, idUser: String, imageLike: ImageView) {
        mLikeProvider = LikeProvider()
        mLikeProvider.getLikeByUserAndPost(idUser, idPost).get()
            .addOnSuccessListener {
                val numberDocuments: Int = it.size()
                Log.d("megustasss1", "$numberDocuments")
                if (numberDocuments > 0) {
                    imageLike.setImageResource(R.drawable.baseline_favorite_24)
                } else {
                    imageLike.setImageResource(R.drawable.baseline_favorite_border_24)
                }
            }
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

    private fun actualizarNLikes(idPost: String, bandera: Boolean) {
        mPublicacionesProvider = PublicacionesProvider()
        mPublicacionesProvider.getPostbyId(idPost).addOnSuccessListener {
            if (it.exists()) {
                if (it.contains("likesCount")) {
                    val n = it.get("likesCount").toString().toLong()
                    val suma: Long = if (bandera) {
                        n + 1
                    } else {
                        n - 1
                    }
                    mPublicacionesProvider.updatePostWithNLikes(idPost, suma)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                            }
                        }
                }
            }
        }
    }
    public class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            Log.d("ViewHolder", "Inicializando ViewHolder")
        }
    }
    fun updateYourList(filteredResults: List<Publicaciones>) {
        proveedorPostList.clear()
        proveedorPostList.addAll(filteredResults)
        notifyDataSetChanged() // Notifica que los datos han cambiado
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.cardview_publicaciones2, parent, false)
        return ViewHolder(view)
    }
}