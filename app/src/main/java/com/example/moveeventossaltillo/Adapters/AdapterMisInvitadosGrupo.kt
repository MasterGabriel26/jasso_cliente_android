package com.example.moveeventossaltillo.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moveeventossaltillo.HomeActivity
import com.example.moveeventossaltillo.Models.InvitadosGrupo
import com.example.moveeventossaltillo.Provider.MisInvitadoProvider
import com.example.moveeventossaltillo.R
import com.example.moveeventossaltillo.utils.RelativeTime
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.text.SimpleDateFormat
import java.util.Locale

class AdapterMisInvitadosGrupo(
    options: FirestoreRecyclerOptions<InvitadosGrupo?>?,
    var context: Context?
) : FirestoreRecyclerAdapter<InvitadosGrupo, AdapterMisInvitadosGrupo.ViewHolder>(options!!) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int, mInvitadosGrupo: InvitadosGrupo) {

        val nombre = holder.itemView.findViewById<TextView>(R.id.nombre)
        val fecha = holder.itemView.findViewById<TextView>(R.id.fecha)
        val invitados = holder.itemView.findViewById<TextView>(R.id.invitados)
        val Proveedor = holder.itemView.findViewById<TextView>(R.id.tv_proveedor)
        val fechaEvento = holder.itemView.findViewById<TextView>(R.id.tv_fecha_del_evento)
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaFormateadaCita = formatoFecha.format(mInvitadosGrupo.fechaEvento)

        Proveedor.text = mInvitadosGrupo.proveedor
        fechaEvento.text = fechaFormateadaCita
        nombre.text=mInvitadosGrupo.nombre
        fecha.text= RelativeTime.RelativeTime.getDateTime(mInvitadosGrupo.fechaCreado)

        holder.itemView.setOnClickListener {
            val intent=Intent(context, HomeActivity::class.java)
            intent.putExtra("idGrupoInvitados",mInvitadosGrupo.id)
            intent.putExtra("uid",mInvitadosGrupo.uid)
            context?.startActivity(intent)
        }
        getInvitados(mInvitadosGrupo,invitados)
    }

    private fun getInvitados(mInvitadosGrupo: InvitadosGrupo, invitados: TextView?) {
        Log.d("ttssd","${mInvitadosGrupo.id} ${mInvitadosGrupo.uid}\"")
        val mMisInvitadoProvider= MisInvitadoProvider()
        mMisInvitadoProvider.getCantidadInvitadosByUser(mInvitadosGrupo.id).addSnapshotListener { value, error ->
            val tam=value?.size()
            Log.d("tam..","$tam")
            invitados?.text=tam.toString()
        }

    }


    public class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            Log.d("ViewHolder", "Inicializando ViewHolder")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.cardview_grupo_invitados, parent, false)
        return ViewHolder(view)
    }
}