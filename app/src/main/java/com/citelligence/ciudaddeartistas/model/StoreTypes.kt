package com.citelligence.ciudaddeartistas.model


open class StoreTypes (name: String, id: String) {

    private var nombreCategoria: String = name
    private var nombreDrawable: String = id

    fun getNombre(): String{ return this.nombreCategoria }

    //fun setNombre(name: String){ this.nombreCategoria = name }

    fun getDrawable(): String{ return this.nombreDrawable }

    //fun setidDrawable(id: Int){ this.nameDrawable = id }

    fun getId(): Int{ return this.nombreCategoria.hashCode() }

}