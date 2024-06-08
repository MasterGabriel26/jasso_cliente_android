package com.example.moveeventossaltillo.Provider


import com.example.moveeventossaltillo.Models.Like
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

class LikeProvider {
    private var mCollection: CollectionReference = FirebaseFirestore.getInstance().collection("likes")
    constructor()
    public fun nuevoLike(like: Like) : Task<Void> {
        val documento:DocumentReference=mCollection.document()
        val id=documento.id
        like.idLike=id.toString()
        return documento.set(like)
    }
    public fun deleteLike(idPost: String) : Task<Void> {
        return mCollection.document(idPost).delete()
    }
    public fun getLikeByUserAndPost(idUser:String,id:String):Query{
        //return mCollection.whereEqualTo("uid",idUser)
        return mCollection.whereEqualTo("idPost",id).whereEqualTo("uid",idUser).limit(1)
    }
    public fun getLikestbyPost(id: String): Query {
        return mCollection.whereEqualTo("idPost", id)
    }
}