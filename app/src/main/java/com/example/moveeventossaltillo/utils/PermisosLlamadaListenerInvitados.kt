package com.example.moveeventossaltillo.utils

import com.example.moveeventossaltillo.Models.InvitadosRegistroLlamadas

interface PermisosLlamadaListenerInvitados {
    fun pedirPermisosListener(
        mInvitadosRegistroLlamadas: InvitadosRegistroLlamadas,
        telefono: String
    )
}