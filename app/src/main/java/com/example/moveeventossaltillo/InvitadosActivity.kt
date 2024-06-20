package com.example.moveeventossaltillo

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moveeventossaltillo.Adapters.AdapterMisPublicaionesProveedor
import com.example.moveeventossaltillo.Adapters.InvitadosAdapter
import com.example.moveeventossaltillo.Models.InvitadosModel
import com.example.moveeventossaltillo.Models.InvitadosRegistroLlamadas
import com.example.moveeventossaltillo.Provider.MisInvitadoProvider
import com.example.moveeventossaltillo.Provider.PublicacionesProvider
import com.example.moveeventossaltillo.databinding.ActivityHomeBinding
import com.example.moveeventossaltillo.databinding.ActivityInvitadosBinding
import com.example.moveeventossaltillo.utils.Constantes
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import java.util.Date

class InvitadosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInvitadosBinding
    var uid = ""
    private var idGrupoInvitados = ""
    var mInvitadosRegistroLlamadas = InvitadosRegistroLlamadas()
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val listInivtados: MutableList<InvitadosModel> = mutableListOf()
    lateinit var mInvitadoProvider: MisInvitadoProvider
    private lateinit var firestoreDb: FirebaseFirestore

    //    private lateinit var postAdapter: InvitadosAdapter
    private var PERMISSION_REQUEST_CODE = 110
    private var totalPersonas: Int = 0
    var llevaNinos: Boolean = false
    private var userListener: ListenerRegistration? = null

    private lateinit var mPublicacionesProvider: PublicacionesProvider
    lateinit var mlinearLayoutManager: LinearLayoutManager
    private lateinit var postAdapter: AdapterMisPublicaionesProveedor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvitadosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listeners()
        mAuth = FirebaseAuth.getInstance()
        firestoreDb = FirebaseFirestore.getInstance()
        mInvitadoProvider = MisInvitadoProvider()

        uid = intent.getStringExtra("uid").toString()
        Log.d("PRUEVA", uid)
        idGrupoInvitados = intent.getStringExtra("idGrupoInvitados").toString()
        Log.d("PRUEVA", idGrupoInvitados)

        val currentUser = mAuth.currentUser
        val uid = currentUser?.uid

        if (uid != null) {
            val userRef = firestore.collection("usuarios").document(uid)
            userListener =
                userRef.addSnapshotListener { snapshot: DocumentSnapshot?, error: FirebaseFirestoreException? ->
                    if (error != null) return@addSnapshotListener
                    if (snapshot != null && snapshot.exists()) {
                        val name = snapshot.getString("name")
                        if (!uid.isNullOrBlank()) {
                            initRecyclerView(uid)
//                            binding.userName.text = name
                        }
                    }
                }
        }
    }

    private fun listeners() {
        binding.backInvitados.setOnClickListener { finish() }
        binding.btnAgregarInvitado.setOnClickListener {
            abrirDialogParaCrearInvitado()
        }
    }

    private fun initRecyclerView(uid: String) {
        var query: Query =
            mInvitadoProvider.getAllMyInvitados(uid, idGrupoInvitados)
        var options: FirestoreRecyclerOptions<InvitadosModel?> =
            FirestoreRecyclerOptions.Builder<InvitadosModel>()
                .setQuery(query, InvitadosModel::class.java).build()
        query.addSnapshotListener { snapshots, e ->
            if (e != null) {
                // Manejar el error
                return@addSnapshotListener
            }

            totalPersonas = 0 // Reiniciar el total de personas
            for (doc in snapshots!!) {

                val invitado = doc.toObject(InvitadosModel::class.java)
                totalPersonas += invitado.personas.toIntOrNull() ?: 0
            }
            // Actualizar el TextView con el total de personas.

        }

        val mAdapterMisInvitados = InvitadosAdapter(options, this)
        binding.rvInvitados.layoutManager = LinearLayoutManager(this)
        binding.rvInvitados.adapter = mAdapterMisInvitados
        mAdapterMisInvitados.startListening()
        mAdapterMisInvitados.notifyDataSetChanged()

    }
//    private fun initRecyclerView(uid: String) {
//        var query: Query = mInvitadoProvider.getAllMyInvitados(uid)
//        var options: FirestoreRecyclerOptions<InvitadosModel?> =
//            FirestoreRecyclerOptions.Builder<InvitadosModel>()
//                .setQuery(query, InvitadosModel::class.java).build()
//
//        query.addSnapshotListener { snapshots, e ->
//            if (e != null) return@addSnapshotListener
//            totalPersonas = 0 // Reiniciar el total de personas
//            for (doc in snapshots!!) {
//                val invitado = doc.toObject(InvitadosModel::class.java)
//                totalPersonas += invitado.personas.toIntOrNull() ?: 0
//            }
//        }
//
//        val mAdapterMisInvitados = InvitadosAdapter(options, this)
//        binding.rvInvitados.layoutManager = LinearLayoutManager(this)
//        binding.rvInvitados.adapter = mAdapterMisInvitados
//        mAdapterMisInvitados.startListening()
//        mAdapterMisInvitados.notifyDataSetChanged()
//    }

    private fun abrirDialogParaCrearInvitado() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_agregar_invitado)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        val animation = AnimationUtils.loadAnimation(this, R.anim.dialog_bounce)
//        dialog.findViewById<View>(R.id.cardProgressDialog).startAnimation(animation)

        val nombre = dialog.findViewById<EditText>(R.id.et_NombreInvitado)
        val telefono = dialog.findViewById<EditText>(R.id.et_TelefonoInvitado)
        val lada = dialog.findViewById<EditText>(R.id.et_Lada)
        val adultos = dialog.findViewById<EditText>(R.id.et_Adultos)
        val niños = dialog.findViewById<EditText>(R.id.et_Niños)
        val si = dialog.findViewById<CheckBox>(R.id.cb_Si)
        val no = dialog.findViewById<CheckBox>(R.id.cb_No)
        val btnInvitar = dialog.findViewById<TextView>(R.id.invitar)
        val titulo = dialog.findViewById<TextView>(R.id.textView4)
        titulo.text = "Nuevo invitado"

        si.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                no.isChecked = false
                niños.visibility = View.VISIBLE
            }
        }

        no.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) si.isChecked = false
            niños.visibility = View.GONE
        }


        btnInvitar.setOnClickListener {
            val mInvitados = InvitadosModel()

            if (si.isChecked) {
                mInvitados.child = true
                mInvitados.niños_cantidad = niños.text.toString().trim()
            } else if (no.isChecked) {
                mInvitados.child = false
                mInvitados.niños_cantidad = "0" // Asignar valor predeterminado si es necesario
            }
//            mInvitados.child = si.isChecked
            mInvitados.nombre = nombre.text.toString().trim()
            mInvitados.telefono = "${lada.text}${telefono.text}"
            mInvitados.personas = adultos.text.toString().trim()
            mInvitados.niños_cantidad = niños.text.toString().trim()
            mInvitados.idGrupoInvitados = idGrupoInvitados
            mInvitados.fechaRegistro = Date().time
            mInvitados.uidCliente = mAuth.uid.toString()
            mInvitados.status = Constantes.StatusInvitados.PENDIENTE_EN_CONFIRMAR
//            mInvitados.idGrupoInvitados = idGrupoInvitados
            if (mInvitados.nombre.isNotEmpty() && mInvitados.telefono.isNotEmpty() && mInvitados.personas.isNotEmpty()) {
                mInvitadoProvider.crearInvitados(mInvitados).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Invitado creado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "2", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, llena todos los campos.", Toast.LENGTH_SHORT)
                    .show()
            }
            dialog.dismiss()
        }

        dialog.show()
    }
}