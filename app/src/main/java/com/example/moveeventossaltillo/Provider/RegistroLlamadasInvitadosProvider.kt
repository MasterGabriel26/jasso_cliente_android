package com.example.moveeventossaltillo.Provider


import android.util.Log
import com.example.moveeventossaltillo.Models.InvitadosRegistroLlamadas
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

class RegistroLlamadasInvitadosProvider {
    private var mCollectionReference: CollectionReference = FirebaseFirestore.getInstance().collection("invitadosRegistroLlamadas")

    public fun crearRegistrolLlamadaInvitado(mInvitadosRegistroLlamadas: InvitadosRegistroLlamadas) : Task<Void> {
        val documento:DocumentReference=mCollectionReference.document()
        val id=documento.id
        Log.d("numberduration2", "${mInvitadosRegistroLlamadas.uidCliente} $id")
        mInvitadosRegistroLlamadas.id=id
        return documento.set(mInvitadosRegistroLlamadas)
    }
    public fun getRegistrolLlamadaByInvitado(idInvitado:String) :Query{
        return mCollectionReference.whereEqualTo("idInvitado",idInvitado)
    }

}