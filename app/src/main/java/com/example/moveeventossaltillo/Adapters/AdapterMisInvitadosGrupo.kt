package com.example.moveeventossaltillo.Adapters

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.Button
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.moveeventossaltillo.HomeActivity
import com.example.moveeventossaltillo.InvitadosActivity
import com.example.moveeventossaltillo.Models.InvitadosGrupo
import com.example.moveeventossaltillo.Provider.InvitadoGrupoProvider
import com.example.moveeventossaltillo.Provider.LugaresProvider
import com.example.moveeventossaltillo.Provider.MisInvitadoProvider
import com.example.moveeventossaltillo.R
import com.example.moveeventossaltillo.utils.CustomArrayAdapter
import com.example.moveeventossaltillo.utils.RelativeTime
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AdapterMisInvitadosGrupo(
    options: FirestoreRecyclerOptions<InvitadosGrupo?>?,
    var context: Context?
) : FirestoreRecyclerAdapter<InvitadosGrupo, AdapterMisInvitadosGrupo.ViewHolder>(options!!) {
    private var provider: InvitadoGrupoProvider = InvitadoGrupoProvider()
    private var selectedDate: Calendar? = null
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var uid = ""
    var proveedor = ""

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
        mInvitadosGrupo: InvitadosGrupo
    ) {
        val nombre = holder.itemView.findViewById<TextView>(R.id.nombre)
        val fecha = holder.itemView.findViewById<TextView>(R.id.fecha)
        val invitados = holder.itemView.findViewById<TextView>(R.id.invitados)
        val Proveedor = holder.itemView.findViewById<TextView>(R.id.tv_proveedor)
        val fechaEvento = holder.itemView.findViewById<TextView>(R.id.tv_fecha_del_evento)
        val cv_eliminar = holder.itemView.findViewById<CardView>(R.id.cv_eliminar)
        val cv_editar = holder.itemView.findViewById<CardView>(R.id.cv_editar)
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaFormateadaCita = formatoFecha.format(mInvitadosGrupo.fechaEvento)

        Proveedor.text = mInvitadosGrupo.proveedor
        fechaEvento.text = fechaFormateadaCita
        nombre.text = mInvitadosGrupo.nombre
        fecha.text = RelativeTime.RelativeTime.getDateTime(mInvitadosGrupo.fechaCreado)
        cv_eliminar.setOnClickListener {
            mostrarDialogoConfirmacionEliminacion(mInvitadosGrupo, holder.itemView.context)
        }
        cv_editar.setOnClickListener {
            abrirDialogParaEditarGrupo(mInvitadosGrupo, holder.itemView.context)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, InvitadosActivity::class.java)
            intent.putExtra("idGrupoInvitados", mInvitadosGrupo.id)
            intent.putExtra("uid", mInvitadosGrupo.uid)
            context?.startActivity(intent)
        }
        getInvitados(mInvitadosGrupo, invitados)
    }

    private fun mostrarDialogoConfirmacionEliminacion(grupo: InvitadosGrupo, context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_delete)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnCancel = dialog.findViewById<TextView>(R.id.btn_dialogCancelarDelete)
        val btnDelete = dialog.findViewById<TextView>(R.id.btn_dialogConfirmDelete)
        val title = dialog.findViewById<TextView>(R.id.tv_dialogOutTitle)
        val description = dialog.findViewById<TextView>(R.id.tv_dialogOutDescription)

        title.text = "Eliminar Invitado"
        description.text = "Estás seguro de eliminar a ${grupo.nombre} de tu lista de invitados?"

        btnCancel.setOnClickListener { dialog.dismiss() }
        btnDelete.setOnClickListener {
            provider.eliminarGrupo(grupo.id).addOnSuccessListener {
                dialog.dismiss()
            }.addOnFailureListener {
                Toast.makeText(context, "Error al eliminar invitado", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun abrirDialogParaEditarGrupo(grupo: InvitadosGrupo, context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_agregar_grupo_invitado)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val nombreEditText = dialog.findViewById<TextInputEditText>(R.id.nombre)
        val btnAgregar = dialog.findViewById<Button>(R.id.button4)
        val sr_pagina = dialog.findViewById<Spinner>(R.id.sr_pagina)
        val calendarView = dialog.findViewById<CalendarView>(R.id.calendarView)
        selectedDate = Calendar.getInstance().apply {
            timeInMillis = grupo.fechaEvento
        }
        calendarView.minDate = System.currentTimeMillis()
        calendarView.setDate(grupo.fechaEvento, false, true)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
        }
        sr_pagina.setPopupBackgroundResource(R.drawable.bg_spinner_outline)
        cargarSpinnerLugar(sr_pagina, context) {
            val adapter = sr_pagina.adapter as? CustomArrayAdapter
            if (adapter != null) {
                val position = adapter.getPosition(grupo.proveedor)
                sr_pagina.setSelection(position)
            }
        }
        sr_pagina.setPopupBackgroundResource(R.drawable.bg_spinner_outline)
//        cargarSpinnerLugar(sr_pagina, context)

        // Setear los datos actuales
        nombreEditText.setText(grupo.nombre)
        sr_pagina.post {
            val adapter = sr_pagina.adapter as? CustomArrayAdapter
            if (adapter != null) {
                val position = adapter.getPosition(grupo.proveedor)
                sr_pagina.setSelection(position)
            }
        }
        calendarView.setDate(grupo.fechaEvento)


        btnAgregar.setOnClickListener {
            val mInvitadosGrupo = InvitadosGrupo()
            mInvitadosGrupo.nombre = nombreEditText.text.toString().trim()
            mInvitadosGrupo.fechaCreado = Date().time
            mInvitadosGrupo.uid = mAuth.uid.toString()
            mInvitadosGrupo.proveedor = proveedor
            mInvitadosGrupo.fechaEvento = selectedDate?.timeInMillis ?: 0
            val nombre = nombreEditText.text.toString().trim()
            val proveedorSeleccionado = sr_pagina.selectedItem.toString()
            val fechaEventoSeleccionada = selectedDate?.timeInMillis ?: 0

            if (nombre.isEmpty()) {
                nombreEditText.error = "El nombre es obligatorio"
            } else if (proveedorSeleccionado.isEmpty() || proveedorSeleccionado == "Lugar de promoción") {
                Toast.makeText(context, "Selecciona un proveedor", Toast.LENGTH_SHORT).show()
            } else if (fechaEventoSeleccionada == 0L) {
                Toast.makeText(context, "Selecciona una fecha", Toast.LENGTH_SHORT).show()
            } else {
                val nuevoGrupoMap = hashMapOf(
                    "nombre" to mInvitadosGrupo.nombre,
                    "uid" to mInvitadosGrupo.uid,
                    "proveedor" to mInvitadosGrupo.proveedor,
                    "fechaCreado" to mInvitadosGrupo.fechaCreado,
                    "fechaEvento" to mInvitadosGrupo.fechaEvento
                )
                if (mInvitadosGrupo.nombre.isNotEmpty() && mInvitadosGrupo.proveedor.isNotEmpty()) {
                    provider.actualizarGrupo(grupo.id, nuevoGrupoMap).addOnCompleteListener {
                        if (it.isSuccessful) {

                        } else {

                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Por favorAgrega el nombre del grupo.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun cargarSpinnerLugar(sr_pagina: Spinner, context: Context, callback: () -> Unit) {
        val mLugaresProvider = LugaresProvider()
        mLugaresProvider.getAllLugares().get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot != null && !querySnapshot.isEmpty) {
                val datosLugar = ArrayList<String>()
                datosLugar.add("Lugar de promoción")
                for (document in querySnapshot.documents) {
                    val lugar = document.getString("nombreLugar")
                    lugar?.let { datosLugar.add(it) }
                }
                val datosLugarArray = datosLugar.toTypedArray()
                val adapter = CustomArrayAdapter(
                    context,
                    android.R.layout.simple_spinner_item,
                    datosLugarArray
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sr_pagina.adapter = adapter
                callback() // Llama al callback para establecer la selección del spinner
            } else {
                Log.d("srPagina", "selectedItem")
            }
        }.addOnFailureListener { exception ->
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
                    proveedor = selectedItem
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun getInvitados(mInvitadosGrupo: InvitadosGrupo, invitados: TextView?) {
        Log.d("ttssd", "${mInvitadosGrupo.id} ${mInvitadosGrupo.uid}\"")
        val mMisInvitadoProvider = MisInvitadoProvider()
        mMisInvitadoProvider.getCantidadInvitadosByUser(mInvitadosGrupo.id)
            .addSnapshotListener { value, error ->
                val tam = value?.size()
                Log.d("tam..", "$tam")
                invitados?.text = tam.toString()
            }

    }


    public class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            Log.d("ViewHolder", "Inicializando ViewHolder")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_grupo_invitados, parent, false)
        return ViewHolder(view)
    }
}