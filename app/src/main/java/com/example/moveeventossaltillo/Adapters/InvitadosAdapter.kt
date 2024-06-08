package com.example.moveeventossaltillo.Adapters

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.moveeventossaltillo.Models.InvitadosModel
import com.example.moveeventossaltillo.Provider.MisInvitadoProvider
import com.example.moveeventossaltillo.R
import com.example.moveeventossaltillo.databinding.ItemInvitadosBinding
import com.example.moveeventossaltillo.utils.Constantes
import com.example.moveeventossaltillo.utils.PermisosLlamadaListenerInvitados
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
class InvitadosAdapter(
    options: FirestoreRecyclerOptions<InvitadosModel?>?,
    var context: Context?
) : FirestoreRecyclerAdapter<InvitadosModel, InvitadosAdapter.ViewHolder>(options!!) {
    private var listInivtados: MutableList<InvitadosModel> = mutableListOf()

    override fun onBindViewHolder(holder: ViewHolder, position: Int, mInvitados: InvitadosModel) {
        Log.d("Adapter", "Binding view holder for position $position")
        holder.bind(mInvitados, position)
    }

    public class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = ItemInvitadosBinding.bind(view)
        private var provider: MisInvitadoProvider = MisInvitadoProvider()
        private lateinit var mAuth: FirebaseAuth
        fun bind(
            model: InvitadosModel,
            position: Int
        ) {
            binding.cvEliminar.setOnClickListener {
                mostrarDialogoConfirmacionEliminacion(itemView.context, model, position)
            }

            mAuth = FirebaseAuth.getInstance()
            val userID = mAuth.currentUser?.uid

            binding.tvNombre.text = model.nombre
            binding.tvTelefono.text = model.telefono
            binding.tvCantidadAdultos.text = model.personas
            binding.tvCantidadNiOs.text = model.niños_cantidad
            binding.textView12.text = when (model.status) {
                Constantes.StatusInvitados.PENDIENTE_EN_CONFIRMAR -> {
                    binding.cardView.setCardBackgroundColor(Color.GRAY)
                    binding.textView12.setTextColor(Color.WHITE)
                    "Pendiente en Confirmar"
                }

                Constantes.StatusInvitados.CONFIRMO -> {
                    binding.cardView.setCardBackgroundColor(Color.GREEN)
                    binding.textView12.setTextColor(Color.BLACK)
                    "Confirmó Invitación"
                }

                Constantes.StatusInvitados.CANCELO -> {
                    binding.cardView.setCardBackgroundColor(Color.RED)
                    binding.textView12.setTextColor(Color.WHITE)
                    "Canceló Invitación"
                }

                else -> {
                    binding.cardView.setCardBackgroundColor(Color.GRAY)
                    binding.textView12.setTextColor(Color.WHITE)
                    "Pendiente en Confirmar"
                }
            }
        }
        private fun mostrarDialogoConfirmacionEliminacion(context: Context, model: InvitadosModel, position: Int) {
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
            description.text = "Estás seguro de eliminar a ${model.nombre} de tu lista de invitados?"

            btnCancel.setOnClickListener { dialog.dismiss() }
            btnDelete.setOnClickListener {
                provider.eliminarInvitado(model.id).addOnSuccessListener {
                    dialog.dismiss()
                }.addOnFailureListener {
                    Toast.makeText(context, "Error al eliminar invitado", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
        private fun abrirDialogParaEditar(context: Context, model: InvitadosModel, position: Int) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId == null) {
                Toast.makeText(itemView.context, "Usuario no autenticado.", Toast.LENGTH_SHORT).show()
                return
            }

            val dialog = Dialog(itemView.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dialog_agregar_invitado)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val btnCancel = dialog.findViewById<TextView>(R.id.btn_dialogCancelarDelete)
            val btnDelete = dialog.findViewById<TextView>(R.id.btn_dialogConfirmDelete)
            val title = dialog.findViewById<TextView>(R.id.tv_dialogOutTitle)
            val description = dialog.findViewById<TextView>(R.id.tv_dialogOutDescription)
//            val nombreEditText = dialog.findViewById<TextInputEditText>(R.id.nombre)
//            val telefonoEditText = dialog.findViewById<TextInputEditText>(R.id.telefono)
//            val personasEditText = dialog.findViewById<TextInputEditText>(R.id.personas)
//            val observacion = dialog.findViewById<TextInputEditText>(R.id.observacion)
//            val checkBoxChilds = dialog.findViewById<CheckBox>(R.id.cb_childs)
//            val ladaTelefono = dialog.findViewById<CountryCodePicker>(R.id.ccp)
//            val btnAgregar = dialog.findViewById<Button>(R.id.button4)

//            titulo.text = "Edita tu invitado"
//            nombreEditText.setText(model.nombre)
//            val telefonodb = model.telefono
//            telefonoEditText.setText(telefonodb?.substring(3))
//            personasEditText.setText(model.personas)
//            observacion.setText(model.observacion)
//            checkBoxChilds.isChecked = model.child

//            btnAgregar.setOnClickListener {
//                val nombre = nombreEditText.text.toString().trim()
//                val telefono = telefonoEditText.text.toString().trim()
//                val personas = personasEditText.text.toString().trim()
//                val telefonConLada = "+${ladaTelefono.selectedCountryCode}${telefono}"
//                val observacion = model.observacion
//                val isChildChecked = checkBoxChilds.isChecked
//                if (nombre.isNotEmpty() && telefono.isNotEmpty() && personas.isNotEmpty()) {
//                    val nuevoInvitadoMap = hashMapOf(
//                        "nombre" to nombre,
//                        "telefono" to telefonConLada,
//                        "personas" to personas,
//                        "observacion" to observacion,
//                        "child" to isChildChecked
//                    )
//
//                    val invitadoRef = FirebaseFirestore.getInstance().collection("invitados").document(userId)
//                        .collection("misInvitados").document(model.id!!)
//                    provider.actualizarInvitado(model.id!!, nuevoInvitadoMap)
//                        .addOnSuccessListener {
//                            Toast.makeText(context, "Datos actualizados correctamente.", Toast.LENGTH_SHORT).show()
//                            dialog.dismiss()
////                            notifyItemChanged(position)
//                        }
//                        .addOnFailureListener { e ->
//                            Toast.makeText(context, "Error al actualizar los datos: ${e.message}", Toast.LENGTH_SHORT).show()
//                            dialog.dismiss()
//                        }
//
//                } else {
//                    Toast.makeText(
//                        itemView.context,
//                        "Por favor, llena todos los campos.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
            dialog.show()
        }

        init {
            Log.d("ViewHolder", "Inicializando ViewHolder")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_invitados, parent, false)
        return ViewHolder(view)
    }


}