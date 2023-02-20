package com.citelligence.ciudaddeartistas.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.citelligence.ciudaddeartistas.R
import com.citelligence.ciudaddeartistas.ShopDetailsActivity
import com.citelligence.ciudaddeartistas.adapter.ShopAdapter
import com.google.firebase.firestore.*
import java.util.*

class RecyclerFragment : Fragment(),View.OnClickListener, ShopAdapter.OnShopSelectedListener {

    private var mFirestore: FirebaseFirestore? = null
    private var mShopsRecycler: RecyclerView? = null
    private val mEmptyView: ViewGroup? = null
    private var mAdapter: ShopAdapter? = null
    private var mQuery: Query? = null
    private var mSearchView: SearchView? = null
    private var searchTerm: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_recycler, container, false)

        mShopsRecycler = v.findViewById(R.id.recycler_shops_fragment)

        mSearchView = v.findViewById(R.id.searchView_frag)
        mSearchView!!.isActivated

        initFirestore()
        getFirestoreQuery()

        initRecyclerView()

        return v

    }

    override fun onStart() {
        super.onStart()

        filter()

        if (mAdapter != null) {
            mAdapter!!.startListening()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mAdapter != null) {
            mAdapter!!.stopListening()
        }
    }

    private fun filter(){
        mSearchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                searchTerm = newText
                mAdapter!!.stopListening()
                if (searchTerm != "") {
                    Search(searchTerm)
                    mAdapter!!.setQuery(mQuery)
                }else{
                    getFirestoreQuery()
                    mAdapter!!.setQuery(mQuery)
                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                searchTerm = query
                mAdapter!!.stopListening()
                if (searchTerm != "") {
                    Search(searchTerm)
                    mAdapter!!.setQuery(mQuery)
                }else{
                    getFirestoreQuery()
                    mAdapter!!.setQuery(mQuery)
                }
                return false
            }
        })
    }

    private fun Search(search: String?): Query? {

        var query : String
        if (search!!.length > 1) {
            val text0 = search.substring(0, 1).toUpperCase(Locale.ROOT)
            val text1 = search.substring(1).toLowerCase(Locale.ROOT)

            query = text0 + text1
        }else {
            val text0 = search.substring(0, 1).toUpperCase(Locale.ROOT)
            query = text0
        }

        mQuery = mFirestore!!.collectionGroup("Tiendas").orderBy("nombre").startAt(query).limit(20)

            //.whereGreaterThanOrEqualTo("nombre", search!!.toUpperCase(Locale.ROOT) + "\uf8ff")
        //.startAt(search!!.toUpperCase()).limit(3)
        //.whereGreaterThanOrEqualTo("nombre","${search!!.toUpperCase()}")
        //.startAt(search!!.toUpperCase())//.endAt(search + "\uf8ff")
        return mQuery
    }

    private fun initFirestore(){
        mFirestore = FirebaseFirestore.getInstance()

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        mFirestore!!.firestoreSettings = settings
    }

    private fun getFirestoreQuery() {
        // To read data from Firetore Database
        mQuery = mFirestore!!.collectionGroup("Tiendas")
    }

    //initRecyclerView  obtiene la vista de las tiendas
    private fun initRecyclerView() {
        if (mQuery == null) {
            Log.w("MAIN", "No query, not initializing RecyclerView")
        }
        mAdapter = object : ShopAdapter(mQuery, this) {
            override fun onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mShopsRecycler!!.setVisibility(View.GONE)
                    mEmptyView!!.setVisibility(View.VISIBLE)
                } else {
                    mShopsRecycler!!.setVisibility(View.VISIBLE)
                    mEmptyView!!.setVisibility(View.GONE)
                }
            }

            override fun onError(e: FirebaseFirestoreException?) {
                Toast.makeText(activity, "Error obteniendo los datos", Toast.LENGTH_SHORT).show()
            }
        }


        //mShopsRecycler!!.layoutManager = LinearLayoutManager(activity)  // Vista en forma de lista
        mShopsRecycler!!.layoutManager = GridLayoutManager(activity, 2)   //Vista en forma de grilla
        mShopsRecycler!!.adapter = mAdapter
    }


    companion object {
        fun newInstance() = RecyclerFragment()
    }

    override fun onClick(v: View?) {

    }

    override fun onShopSelected(shop: DocumentSnapshot?) {
        val intent = Intent(activity, ShopDetailsActivity::class.java).apply {
            putExtra("nombre", shop!!["nombre"].toString())
            putExtra("tel", shop["telefono"].toString())
            putExtra("descripcion", shop["descripcion"].toString())
            putExtra("logo",shop["logo"].toString())
            putExtra("productos",shop["productos"].toString())
            putExtra("direccion",shop["direccion"].toString())
            putExtra("contacto",shop["contacto"].toString())
            putExtra("mail",shop["mail"].toString())
            putExtra("celular",shop["celular"].toString())
            putExtra("web",shop["web"].toString())
            putExtra("fb",shop["fb"].toString())
            putExtra("ins",shop["ins"].toString())
            putExtra("yt",shop["yt"].toString())
            putExtra("video",shop["video"].toString())
            putExtra("categoria",shop["categoria"].toString())
        }
        startActivity(intent)
    }
}