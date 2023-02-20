package com.citelligence.ciudaddeartistas.model

data class Shop(
    private var nombre: String? = null,
    private var descripcion: String? = null,
    private var logo: String? = null,
    private var nit: String? = null,
    private var telefono: String? = null,
    private var celular: String? = null,
    private var direccion: String? = null,
    private var mail: String? = null,
    private var productos: String? = null,
    private var web: String? = null,
    private var fb: String? = null,
    private var ins: String? = null,
    private var yt: String? = null,
    private var contacto: String? = null,
    private var video: String? = null,
    private var categoria: String? = null) {


    fun getNombre(): String{return this.nombre!!}
    //fun setNombre(name: String){this.nombre = name}

    fun getDescripcion(): String {return this.descripcion!!}
    //fun setTelefono(tel: String){this.descripcion = tel}

    fun getLogo(): String {return this.logo!!}

    fun getNIT(): String {return this.nit!!}
    //fun setCategoria(cat: String){this.categoria = cat}

    fun getWEB(): String {return this.web!!}

    fun getFB(): String {return this.fb!!}

    fun getYT(): String {return this.yt!!}

    fun getVideo(): String {return this.video!!}

    fun getINSTA(): String {return this.ins!!}

    fun getCEL(): String {return this.celular!!}

    fun getCAT(): String {return this.categoria!!}

}