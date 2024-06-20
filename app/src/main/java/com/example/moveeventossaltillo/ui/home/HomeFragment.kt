package com.example.moveeventossaltillo.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moveeventossaltillo.Adapters.AdapterMisPublicaionesProveedor
import com.example.moveeventossaltillo.Models.InvitadosModel
import com.example.moveeventossaltillo.Models.InvitadosRegistroLlamadas
import com.example.moveeventossaltillo.Models.Publicaciones
import com.example.moveeventossaltillo.Provider.MisInvitadoProvider
import com.example.moveeventossaltillo.Provider.PublicacionesProvider
import com.example.moveeventossaltillo.databinding.FragmentHomeBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance()
        firestoreDb = FirebaseFirestore.getInstance()
        mInvitadoProvider = MisInvitadoProvider()
        mPublicacionesProvider = PublicacionesProvider()
        mlinearLayoutManager = LinearLayoutManager(requireContext())

        listeners()
        initRecyclerViewPaquetes()

        var intent = Intent()
        uid = intent.getStringExtra("uid").toString()
        idGrupoInvitados = intent.getStringExtra("idGrupoInvitados").toString()
        Log.d("Queri", uid)
        Log.d("idGrupoInvitados", uid)
        val root: View = binding.root
        val currentUser = mAuth.currentUser
        val uid = currentUser?.uid

        if (uid != null) {
            val userRef = firestore.collection("usuarios").document(uid)
            userListener = userRef.addSnapshotListener { snapshot: DocumentSnapshot?, error: FirebaseFirestoreException? ->
                    if (error != null) return@addSnapshotListener
                    if (snapshot != null && snapshot.exists()) {
                        val name = snapshot.getString("name")
                        if (!uid.isNullOrBlank()) {
//                            initRecyclerView(uid)
                            binding.userName.text = name
                        }
                    }
                }
        }

//        initRecyclerView("PrIPd2MOifXRE7rG1OztFUVhinE2")
//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    private fun initRecyclerViewPaquetes() {
        binding.rvInvitados.layoutManager = this.mlinearLayoutManager
        var query: Query = mPublicacionesProvider.getAllPosts()
        Log.d("Queri", query.toString())
        val options: FirestoreRecyclerOptions<Publicaciones?> = FirestoreRecyclerOptions
            .Builder<Publicaciones>()
            .setQuery(query, Publicaciones::class.java)
            .build()
        postAdapter = AdapterMisPublicaionesProveedor(options, requireContext())
        Log.d("Queri",postAdapter.toString())
        binding.rvInvitados.adapter = this.postAdapter
        postAdapter.startListening()
        postAdapter.notifyDataSetChanged()
    }

    private fun listeners() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onStart() {
        super.onStart()
        postAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        if (postAdapter != null) {
            postAdapter.stopListening()
        }
    }



}