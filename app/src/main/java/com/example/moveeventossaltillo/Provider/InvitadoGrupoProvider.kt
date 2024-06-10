package com.example.moveeventossaltillo.Provider

import com.example.moveeventossaltillo.Models.InvitadosGrupo
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class InvitadoGrupoProvider() {
    private val db = FirebaseFirestore.getInstance()
    private var userId: String? = null
    private var mCollectionReference: CollectionReference = db.collection("invitadosGrupos")


    public fun crearGrupo(mInvitadosGrupo: InvitadosGrupo): Task<Void> {
        val documento = mCollectionReference.document()
        val id = documento.id
        mInvitadosGrupo.id = id.toString()
        return documento.set(mInvitadosGrupo)
    }
    public fun getAllMyInvitados(uidCliente: String): Query {
        return mCollectionReference
            .whereEqualTo("uid", uidCliente)
            .orderBy("fechaCreado", Query.Direction.DESCENDING)
    }
    public fun getAllGroup(): Query {
        return mCollectionReference

    }

}
