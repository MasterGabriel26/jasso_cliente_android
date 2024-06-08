package com.example.moveeventossaltillo.utils

import android.app.Application

class Constantes {
     object Rol:Application(){
        val ADMIN="admin"
        val PROVEEDOR="proveedor"
        val USER="user"
        val ASESOR="asesor"
         val LIDER="lider"
         val CLIENTE="cliente"
    }
    object TypeNotification : Application() {
        var MENSAJE="MENSAJE"// 0
        var SEGUIMIENTO="SEGUIMIENTO"//1
        var CITA_CANCELADA="CITA_CANCELADA"//2
        var CITA_AGENDADA="CITA_AGENDADA"//2
        var APROBACION="APROBACION"//3
        var RECHAZADO="RECHAZADO"//3
        var CAMBIO_ROL="CAMBIO_ROL"//3
        var VIDEO="VIDEO"//3
    }
    object StatusInvitados:Application(){
        var PENDIENTE_EN_CONFIRMAR="PENDIENTE_EN_CONFIRMAR"//4
        var CONFIRMO="CONFIRMO_EL_INVITADO"//5
        var CANCELO="CANCELO_EL_INVITADO"//6
    }
    object DatosUsuario : Application() {
        var uidLider = ""
        fun agregaLider(uidLider: String) {
            this.uidLider=uidLider
        }

    }

    companion object{
        val CANTIDAD_DE_PASOS=13
    }
}