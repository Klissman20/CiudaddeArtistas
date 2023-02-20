package com.citelligence.ciudaddeartistas.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.citelligence.ciudaddeartistas.R
import com.citelligence.ciudaddeartistas.model.StoreTypes

class StoreTypesAdapter  (context: Context, stores: Array<StoreTypes>): BaseAdapter() {

    private var context = context
    private var stores = stores

    override fun getCount(): Int { return stores.size }

    override fun getItem(position: Int): StoreTypes { return stores[position] }

    override fun getItemId(position: Int): Long { return getItem(position).getId().toLong() }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        var myview = view
        if (myview == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            myview = inflater.inflate(R.layout.grid_view, viewGroup, false)
        }
        val imagenTipo = myview!!.findViewById(R.id.imagen_tipo) as ImageView
        val nombreTipo = myview.findViewById(R.id.nombre_tipo) as TextView
        val item: StoreTypes = getItem(position)
        imagenTipo.setImageResource(context.getResources().getIdentifier(item.getDrawable(), "drawable", context.getPackageName()))
        nombreTipo.setText(item.getNombre())
        return myview
    }

}