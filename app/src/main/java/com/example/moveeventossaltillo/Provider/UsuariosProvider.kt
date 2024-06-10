package com.example.moveeventossaltillo.Provider

import com.example.moveeventossaltillo.Models.Usuarios
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query


class UsuariosProvider {
    private var mCollectionReference: CollectionReference = FirebaseFirestore.getInstance().collection("usuarios")
    public fun crearUsuario(usuario: Usuarios) : Task<Void> {
        return mCollectionReference.document(usuario.uid!!).set(usuario)
    }
    public fun getRol(rol:String): Query {
        return mCollectionReference.whereEqualTo("userType",rol)
    }

    public fun getAsesoresByLider(uidLider: String): Query {
        return mCollectionReference.whereEqualTo("uidLider",uidLider)
    }

    public fun getUsuario(uid:String) :Task<DocumentSnapshot> {
        return mCollectionReference.document(uid).get()
    }
    public fun getAllUsuario() :Query {
        return mCollectionReference
    }
    public fun getAllAsesor(): Query {
        return mCollectionReference.whereEqualTo("userType","asesor").orderBy("name", Query.Direction.ASCENDING)
    }
    public fun getAllLider(): Query {
        return mCollectionReference.whereEqualTo("userType","lider").orderBy("name", Query.Direction.ASCENDING)
    }
    public fun getAllLiderAndAsesor(): Query {
        return mCollectionReference
            .whereIn("userType", listOf("lider", "asesor"))
            .orderBy("name", Query.Direction.ASCENDING)
    }

    public fun updateRols(uid:String,userTypeSolicitud:String): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()
        map["userTypeSolicitud"] = userTypeSolicitud
        return mCollectionReference.document(uid).update(map)
    }
    public fun updateLider(uid:String,uidLider:String): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()
        map["uidLider"] = uidLider
        return mCollectionReference.document(uid).update(map)
    }
    public fun updateRolAprobado(uid:String,newRol:String): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()
        map["userTypeSolicitud"] = ""
        map["userType"] = newRol
        return mCollectionReference.document(uid).update(map)
    }
    public fun getAllSolicitudCambioRol(): Query {
        return mCollectionReference.whereNotEqualTo("userTypeSolicitud", "")
    }
}