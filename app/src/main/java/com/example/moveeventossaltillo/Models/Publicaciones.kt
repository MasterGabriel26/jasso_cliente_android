package com.example.moveeventossaltillo.Models

import java.io.Serializable

class Publicaciones : Serializable {

    public lateinit var id: String
    public lateinit var uid: String
    public var fechaPost: Long = 0
    public var fechaDeEvento: Long = 0
    public var categoria: String= ""
    public var tituloEvento: String = ""
    public var tituloEventoLowercase: String = ""
    public var likesCount: Long = 0
    public var tipoEvento: ArrayList<String>? = ArrayList()
    public var descripcion: String = ""
    public var lugarDelEventoMapa: String = ""
    public var celularContacto: String = ""
    public var multimediaUrl: ArrayList<String>? = ArrayList()
    public var multimediaName: ArrayList<String>? = ArrayList()
    public var horarioEvento: String = ""
    public var valorAgregado: String = ""

    //paquete
    public var anticipo: String = ""
    public var costoPaquete: String = ""
    public var costoParaFirmar: String = ""

    // info para asesor
    public var comisionVenta: String = ""
    public var comisionLlamada: String = ""
    public var comisionLiderVenta: String = ""

    public var cantidadDePersonas: String = ""
    public var fechaVigencia: Long? = 0

    public var active=true
    //------------------Alimentos-----------------------------
    public  var serviciosAlimento: ArrayList<String>? = ArrayList()
    public var cantidadBotellas: String = ""
    public var tipoBotellas: ArrayList<String>? = ArrayList()
    public var urlFotoMesa: String = ""
    public var urlFotoSilla: String = ""
    public var urlFotoMontaje: String = ""

    //------------------Renta de Vestidos------------
    var tipoTela: ArrayList<String>? = ArrayList()
    public var colores: ArrayList<String>? = ArrayList()
    public var costoFleteVestido: String = ""
    public var descripcionAjusteVestido: String = ""
    public var fechaEntregaVestido: Long = 0
    public var modeloVestidoLista: ArrayList<String>? = ArrayList()
    public var costoDiaAdicionalVestido: String = ""
    public var cuponPromocional: String = ""
    public var entregaDomicio: Boolean=false

    //------------------Fotografia y Video-------
    public var tipoPapel: ArrayList<String>? = ArrayList()
    public var tamanio: ArrayList<String>? = ArrayList()
    public var incluyeDomicilioMaquillaje: Boolean=false
    public var incluyeMaquillaje: Boolean=false
    public var tieneMisa: Boolean=false
    public var bodaCivil: Boolean=false
    public var direccionBoda: String = ""
    public var horaMisa: String = ""
    public var direccionMisa: String = ""
    //------------------Decoración-------
    public var lugar: String = ""
    public var urlFotoArreglos: String = ""
    public var urlFotoBase: String = ""
    public var urlFotoTonosEvento: String = ""

    //------------------Grupos y Sonido----------------------------
    public var costoHora: String = ""
    public var generos: ArrayList<String>? = ArrayList()

    //------------------Renta de Mobiliario----------------------------
    public var costoDia: String = ""
    public var costoFleteMoviliario: String = ""

    public var id_material: String = ""

/* ALIMENTOS Y BEBIDAS
- titulo**
- tipo de evento**
- cantidad de personas**
- vigencia**
- fecha de promoción**
- servicios**
- tipo de mesa**
- tipo de silla**
- tipo montaje**
- precio del paquete**
- anticipo**
- costo del contrato**
- comision de venta*
- comisión líder de venta*
- comisión de llamada*
- valor agregado*
- direccion*
- telefono*
- descripción**/
}