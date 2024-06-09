package com.example.moveeventossaltillo.Provider

import com.example.moveeventossaltillo.Models.InvitadosModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MisInvitadoProvider {
    private val db = FirebaseFirestore.getInstance()
    private var mCollectionReference: CollectionReference = db.collection("invitados")
    public fun crearInvitados(mInvitados: InvitadosModel): Task<Void> {
        val documento = mCollectionReference.document()
        val id = documento.id
        mInvitados.id = documento.id
        return documento.set(mInvitados)
    }
    public fun getAllMyInvitados(uidCliente: String, idGrupoInvitados: String?): Query {
        return mCollectionReference
            .whereEqualTo("uidCliente", uidCliente)
            .whereEqualTo("idGrupoInvitados", idGrupoInvitados)
            .orderBy("fechaRegistro", Query.Direction.DESCENDING)
    }
    public fun getCantidadInvitadosByUser(idGrupoInvitados: String): Query {
        return mCollectionReference
            .whereEqualTo("idGrupoInvitados", idGrupoInvitados)
    }
    fun actualizarInvitado(idInvitado: String, map: Map<String, Any>): Task<Void> {
        return mCollectionReference.document(idInvitado).update(map)
    }
    fun eliminarInvitado(idInvitado: String): Task<Void> {
        return mCollectionReference.document(idInvitado).delete()
    }
}