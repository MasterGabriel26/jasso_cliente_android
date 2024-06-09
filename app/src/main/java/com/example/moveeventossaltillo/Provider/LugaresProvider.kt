package com.example.moveeventossaltillo.Provider


import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class LugaresProvider {
    private var mCollectionReference: CollectionReference = FirebaseFirestore.getInstance().collection("lugares")
    constructor()
    public fun getAsesor(uid:String) :Task<DocumentSnapshot> {
        return mCollectionReference.document(uid).get()
    }
    public fun getAllLugares(): Query {
        return mCollectionReference.orderBy("nombreLugar", Query.Direction.DESCENDING)
    }
    public fun getLugaresById(id:String): Query {
        return mCollectionReference.orderBy("nombreLugar", Query.Direction.DESCENDING)
    }
}