package com.example.moveeventossaltillo

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Button
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moveeventossaltillo.Adapters.AdapterMisInvitadosGrupo
import com.example.moveeventossaltillo.Models.InvitadosGrupo
import com.example.moveeventossaltillo.Provider.InvitadoGrupoProvider
import com.example.moveeventossaltillo.Provider.LugaresProvider
import com.example.moveeventossaltillo.databinding.ActivityGruposInvitadosBinding
import com.example.moveeventossaltillo.utils.CustomArrayAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import java.util.Calendar
import java.util.Date

class GruposInvitadosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGruposInvitadosBinding
    lateinit var mInvitadoGrupoProvider: InvitadoGrupoProvider
    private lateinit var mAuth: FirebaseAuth
    private var selectedDate: Calendar? = null
    var uid = ""
    var proveedor = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGruposInvitadosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mInvitadoGrupoProvider = InvitadoGrupoProvider()
        mAuth = FirebaseAuth.getInstance()

        uid = intent.getStringExtra("uid").toString()
        if (uid == "null" || uid == null || uid == "") {
            //mis grupos, soy cliente
            binding.btnAgregarGrupo.visibility = View.VISIBLE
            initRecyclerView(mAuth.uid.toString())
        } else {
            binding.btnAgregarGrupo.visibility = View.GONE
//acá debo ocultar lo que no se necesita como asesor o admin...
            initRecyclerView(uid)
        }
        listeners()
        binding.btnAgregarGrupo.setOnClickListener { abrirDialogParaCrearGrupo() }

    }
    private fun listeners() {
        binding.ichome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        binding.grupoInvitados.setOnClickListener {
            startActivity(Intent(this, GruposInvitadosActivity::class.java))
        }
    }
    private fun abrirDialogParaCrearGrupo() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_agregar_grupo_invitado)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val nombreEditText = dialog.findViewById<TextInputEditText>(R.id.nombre)
        val btnAgregar = dialog.findViewById<Button>(R.id.button4)
        val sr_pagina = dialog.findViewById<Spinner>(R.id.sr_pagina)
        val calendarView = dialog.findViewById<CalendarView>(R.id.calendarView)
        calendarView.minDate = System.currentTimeMillis()
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
        }
        sr_pagina.setPopupBackgroundResource(R.drawable.bg_spinner_outline)
        cargarSpinnerLugar(sr_pagina)

        btnAgregar.setOnClickListener {
            val mInvitadosGrupo = InvitadosGrupo()
            mInvitadosGrupo.nombre = nombreEditText.text.toString().trim()
            mInvitadosGrupo.fechaCreado = Date().time
            mInvitadosGrupo.uid = mAuth.uid.toString()
            mInvitadosGrupo.proveedor = proveedor
            mInvitadosGrupo.fechaEvento = selectedDate?.timeInMillis ?: 0

            if (mInvitadosGrupo.nombre.isNotEmpty() && mInvitadosGrupo.proveedor.isNotEmpty()) {
                mInvitadoGrupoProvider.crearGrupo(mInvitadosGrupo).addOnCompleteListener {
                    if (it.isSuccessful) {

                    } else {

                    }
                }
            } else {
                Toast.makeText(this, "Por favorAgrega el nombre del grupo.", Toast.LENGTH_SHORT)
                    .show()
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun cargarSpinnerLugar(sr_pagina: Spinner) {
        val mLugaresProvider = LugaresProvider()
        mLugaresProvider.getAllLugares().get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot != null && !querySnapshot.isEmpty) {
                Log.d("srPagina1", "selectedItem")
                // Lista para almacenar los nombres de los asesores
                val datosLugar = ArrayList<String>()
                // Añadir hint como primer elemento
                datosLugar.add("Lugar de promoción")
                // Iterar sobre los documentos y agregar nombres a la lista
                for (document in querySnapshot.documents) {
                    val lugar = document.getString("nombreLugar")
                    lugar?.let {
                        datosLugar.add(it)
                    }
                }
                // Convertir la lista a un Array
                val datosLugarArray = datosLugar.toTypedArray()
                // Crear el ArrayAdapter con el Array
                val adapter =
                    CustomArrayAdapter(this, android.R.layout.simple_spinner_item, datosLugarArray)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Establecer el adaptador en el Spinner
                sr_pagina.adapter = adapter
            } else {
                Log.d("srPagina", "selectedItem")
            }
        }.addOnFailureListener { exception ->
            // Manejar errores al obtener datos de Firebase
            Log.e("Error", "Error al obtener eventos: ${exception.message}")
        }
        sr_pagina.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    proveedor = selectedItem.toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No hacer nada
            }
        }
    }


    private fun initRecyclerView(uid: String) {
        var query: Query = mInvitadoGrupoProvider.getAllMyInvitados(uid)
        var options: FirestoreRecyclerOptions<InvitadosGrupo?> =
            FirestoreRecyclerOptions.Builder<InvitadosGrupo>()
                .setQuery(query, InvitadosGrupo::class.java).build()
        binding.rvGrupos.layoutManager = LinearLayoutManager(this)
        val mAdapterMisInvitadosGrupo = AdapterMisInvitadosGrupo(options, this)
        binding.rvGrupos.adapter = mAdapterMisInvitadosGrupo
        mAdapterMisInvitadosGrupo.startListening()
        mAdapterMisInvitadosGrupo.notifyDataSetChanged()

    }
}