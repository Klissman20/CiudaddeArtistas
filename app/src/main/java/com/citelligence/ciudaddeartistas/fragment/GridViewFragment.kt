package com.citelligence.ciudaddeartistas.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.citelligence.ciudaddeartistas.R
import com.citelligence.ciudaddeartistas.ShopViewActivity
import com.citelligence.ciudaddeartistas.adapter.StoreTypesAdapter
import com.citelligence.ciudaddeartistas.model.StoreTypes
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

/**
 * A simple [Fragment] subclass.
 * Use the [GridViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GridViewFragment: Fragment() {

    private var mFirestore: FirebaseFirestore? = null
    private var gridView: GridView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_gridview, container, false)

        gridView = v.findViewById(R.id.grid_fragment)

        initFirestore()

        //Interfaz de captura los datos de la consulta hecha a firestore para inflar la vista de las categorias
        getCategories(object : FirestoreCallBack {
            override fun onCallBack(arrayCategories: Array<StoreTypes>) {
                val storeAdapter = context?.let {
                    StoreTypesAdapter(it, arrayCategories)
                }
                gridView!!.adapter = storeAdapter
            }
        }, mFirestore!!)

        gridListener(gridView)//Funcion de escucha para clicks en gridView(Vista de categorias)

        return v

    }

    private fun initFirestore() {
        mFirestore = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        mFirestore!!.firestoreSettings = settings
    }

    private fun gridListener(gridView: GridView?){
        gridView!!.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position) as StoreTypes
                set_query(selectedItem.getNombre())
            }
        }
    }

    private fun set_query(name: String){
        val query = name
        val intent = Intent(activity, ShopViewActivity::class.java).apply {
            putExtra("query", query)
        }
        startActivity(intent)
    }

    //Funcion para obtener las categorias de la vista principal
    private fun getCategories(CallBack: FirestoreCallBack, database: FirebaseFirestore){
        var category: Array<StoreTypes>
        var index = 0
        val db = database
        val negociosRef = db.collection("/Negocios")

        negociosRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                category = Array<StoreTypes>(size = task.result!!.size()){
                    StoreTypes("", "")
                }
                for (document in task.result!!) {
                    category[index] = StoreTypes(document.id, document["drawable"].toString())
                    index++
                }
                CallBack.onCallBack(category) //llamado a interfaz de devolucion asincrona
            }
        }
    }

    //Interfaz de devolucion de llamada de los datos en Firestore ^^ asincrona
    interface FirestoreCallBack{
        fun onCallBack(arrayCategories: Array<StoreTypes>)
    }

    companion object {
        fun newInstance() = GridViewFragment()
    }
}