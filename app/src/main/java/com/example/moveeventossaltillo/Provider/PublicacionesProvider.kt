package com.example.moveeventossaltillo.Provider

import android.util.Log
import com.example.moveeventossaltillo.Models.Publicaciones
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query

class PublicacionesProvider {
    private var mCollectionReference: CollectionReference = FirebaseFirestore.getInstance().collection("publicaciones")
    constructor()
    /*public fun crearPublicacion(publicaciones: Publicaciones): Task<Void> {
        try {
            val id = mCollectionReference.document().id
            publicaciones.id = id
            Log.d("pub3", id.toString())
            return mCollectionReference.document(id).set(publicaciones)
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirestoreException", "Error al escribir en Firestore: ${e.message}", e)
            throw e  // Lanza la excepción nuevamente después de registrarla
        } catch (e: Exception) {
            Log.e("Exception", "Error general: ${e.message}", e)
            throw e  // Lanza la excepción nuevamente después de registrarla
        }
    }
*/
    public fun crearPublicacion(publicaciones: Publicaciones): Task<Void> {
        try {
            val id = mCollectionReference.document().id
            publicaciones.id = id
            Log.d("pub3", id.toString())
            return mCollectionReference.document(id).set(publicaciones)
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirestoreException", "Error al escribir en Firestore: ${e.message}", e)
            throw e  // Lanza la excepción nuevamente después de registrarla
        } catch (e: Exception) {
            Log.e("Exception", "Error general: ${e.message}", e)
            throw e  // Lanza la excepción nuevamente después de registrarla
        }
    }
    public fun updatePostVideos(publicaciones: Publicaciones): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()
        map["multimediaUrl"] =ArrayList( publicaciones.multimediaUrl)
        return mCollectionReference.document(publicaciones.id!!).update(map)
    }
    public fun updatePost(publicaciones: Publicaciones): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()

        if (publicaciones.id.isNotEmpty()) map["id"] = publicaciones.id
        if (publicaciones.uid.isNotEmpty()) map["uid"] = publicaciones.uid
        if (publicaciones.fechaPost != 0L) map["fechaPost"] = publicaciones.fechaPost
        if (publicaciones.fechaDeEvento != 0L) map["fechaDeEvento"] = publicaciones.fechaDeEvento
        if (publicaciones.categoria.isNotEmpty()) map["categoria"] = publicaciones.categoria
        if (publicaciones.tituloEvento.isNotEmpty()) map["tituloEvento"] = publicaciones.tituloEvento
        if (publicaciones.tituloEventoLowercase.isNotEmpty()) map["tituloEventoLowercase"] = publicaciones.tituloEventoLowercase
        if (publicaciones.likesCount != 0L) map["likesCount"] = publicaciones.likesCount
        publicaciones.tipoEvento?.let { if (it.isNotEmpty()) map["tipoEvento"] = it }
        if (publicaciones.descripcion.isNotEmpty()) map["descripcion"] = publicaciones.descripcion
        if (publicaciones.lugarDelEventoMapa.isNotEmpty()) map["lugarDelEventoMapa"] = publicaciones.lugarDelEventoMapa
        if (!publicaciones.celularContacto.isNullOrEmpty()) map["celularContacto"] = publicaciones.celularContacto
        if (publicaciones.horarioEvento.isNotEmpty()) map["horarioEvento"] = publicaciones.horarioEvento
        if (publicaciones.valorAgregado.isNotEmpty()) map["valorAgregado"] = publicaciones.valorAgregado
        if (publicaciones.anticipo.isNotEmpty()) map["anticipo"] = publicaciones.anticipo
        if (publicaciones.costoPaquete.isNotEmpty()) map["costoPaquete"] = publicaciones.costoPaquete
        if (publicaciones.costoParaFirmar.isNotEmpty()) map["costoParaFirmar"] = publicaciones.costoParaFirmar
        if (publicaciones.comisionVenta.isNotEmpty()) map["comisionVenta"] = publicaciones.comisionVenta
        if (publicaciones.comisionLlamada.isNotEmpty()) map["comisionLlamada"] = publicaciones.comisionLlamada
        if (publicaciones.comisionLiderVenta.isNotEmpty()) map["comisionLiderVenta"] = publicaciones.comisionLiderVenta
        if (publicaciones.cantidadDePersonas.isNotEmpty()) map["cantidadDePersonas"] = publicaciones.cantidadDePersonas
        if (publicaciones.fechaVigencia != null && publicaciones.fechaVigencia != 0L) map["fechaVigencia"] =
            publicaciones.fechaVigencia!!
        map["active"] = publicaciones.active
        publicaciones.multimediaUrl?.let { if (it.isNotEmpty()) map["multimediaUrl"] = it }
        publicaciones.multimediaName?.let { if (it.isNotEmpty()) map["multimediaName"] = it }
        if (publicaciones.cantidadBotellas.isNotEmpty()) map["cantidadBotellas"] = publicaciones.cantidadBotellas
        if (publicaciones.urlFotoMesa.isNotEmpty()) map["urlFotoMesa"] = publicaciones.urlFotoMesa
        if (publicaciones.urlFotoSilla.isNotEmpty()) map["urlFotoSilla"] = publicaciones.urlFotoSilla
        if (publicaciones.urlFotoMontaje.isNotEmpty()) map["urlFotoMontaje"] = publicaciones.urlFotoMontaje
        publicaciones.tipoTela?.let { if (it.isNotEmpty()) map["tipoTela"] = it }
        publicaciones.colores?.let { if (it.isNotEmpty()) map["colores"] = it }
        if (publicaciones.costoFleteVestido.isNotEmpty()) map["costoFleteVestido"] = publicaciones.costoFleteVestido
        if (publicaciones.descripcionAjusteVestido.isNotEmpty()) map["descripcionAjusteVestido"] = publicaciones.descripcionAjusteVestido
        if (publicaciones.fechaEntregaVestido != 0L) map["fechaEntregaVestido"] = publicaciones.fechaEntregaVestido
        publicaciones.modeloVestidoLista?.let { if (it.isNotEmpty()) map["modeloVestidoLista"] = it }
        if (publicaciones.costoDiaAdicionalVestido.isNotEmpty()) map["costoDiaAdicionalVestido"] = publicaciones.costoDiaAdicionalVestido
        if (publicaciones.cuponPromocional.isNotEmpty()) map["cuponPromocional"] = publicaciones.cuponPromocional
        map["entregaDomicio"] = publicaciones.entregaDomicio
        publicaciones.tipoPapel?.let { if (it.isNotEmpty()) map["tipoPapel"] = it }
        publicaciones.tamanio?.let { if (it.isNotEmpty()) map["tamanio"] = it }
        map["incluyeDomicilioMaquillaje"] = publicaciones.incluyeDomicilioMaquillaje
        map["incluyeMaquillaje"] = publicaciones.incluyeMaquillaje
        map["tieneMisa"] = publicaciones.tieneMisa
        map["bodaCivil"] = publicaciones.bodaCivil
        if (publicaciones.direccionBoda.isNotEmpty()) map["direccionBoda"] = publicaciones.direccionBoda
        if (publicaciones.horaMisa.isNotEmpty()) map["horaMisa"] = publicaciones.horaMisa
        if (publicaciones.direccionMisa.isNotEmpty()) map["direccionMisa"] = publicaciones.direccionMisa
        if (publicaciones.lugar.isNotEmpty()) map["lugar"] = publicaciones.lugar
        if (publicaciones.urlFotoArreglos.isNotEmpty()) map["urlFotoArreglos"] = publicaciones.urlFotoArreglos
        if (publicaciones.urlFotoBase.isNotEmpty()) map["urlFotoBase"] = publicaciones.urlFotoBase
        if (publicaciones.urlFotoTonosEvento.isNotEmpty()) map["urlFotoTonosEvento"] = publicaciones.urlFotoTonosEvento
        if (publicaciones.costoHora.isNotEmpty()) map["costoHora"] = publicaciones.costoHora
        publicaciones.generos?.let { if (it.isNotEmpty()) map["generos"] = it }
        if (publicaciones.costoDia.isNotEmpty()) map["costoDia"] = publicaciones.costoDia
        if (publicaciones.costoFleteMoviliario.isNotEmpty()) map["costoFleteMoviliario"] = publicaciones.costoFleteMoviliario
        if (publicaciones.id_material.isNotEmpty()) map["id_material"] = publicaciones.id_material

        return mCollectionReference.document(publicaciones.id).update(map)
    }



    public fun getMyPosts(id: String): Query {
        return mCollectionReference
            .whereEqualTo("uid", id)
            .orderBy("fechaPost", Query.Direction.DESCENDING)
    }
    public fun getAllPostsMoreLike(): Query {
        return mCollectionReference.orderBy("likesCount", Query.Direction.DESCENDING).orderBy("fechaPost", Query.Direction.DESCENDING)
    }
    public fun updatePostWithNLikes(idPost:String,nLikes:Long): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()
        map["likesCount"] = nLikes
        return mCollectionReference.document(idPost!!).update(map)
    }
    public fun getAllPosts(): Query {
//        return mCollectionReference
        return mCollectionReference.orderBy("fechaPost", Query.Direction.DESCENDING)
    }
    public fun deletePost(idpost: String) : Task<Void> {
        return mCollectionReference.document(idpost).delete()
    }
    public fun getPostbyId(id: String): Task<DocumentSnapshot> {
        return mCollectionReference.document(id).get()
    }
    public fun deleteMultimediaByIdPost(idPost: String,multimedia:ArrayList<String>): Task<Void>  {
        val map: MutableMap<String, Any> = HashMap()
        map["multimediaUrl"] = multimedia
        return mCollectionReference.document(idPost!!).update(map)
    }
    /*fun getPublicacionesBySearchText(searchText: String): Query {
        return mCollectionReference.whereGreaterThanOrEqualTo("descripcion", searchText)
    }*/
    fun getPublicacionesBySearchText(searchText: String): Query {
        return mCollectionReference
            .whereGreaterThanOrEqualTo("descripcion", searchText)
            .whereLessThanOrEqualTo("descripcion", searchText + "\uf8ff")
    }

    // En PublicacionesProvider
    fun getCollectionReference(): CollectionReference {
        return mCollectionReference
    }


}