package com.citelligence.ciudaddeartistas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.citelligence.ciudaddeartistas.adapter.ShopAdapter
import com.google.firebase.firestore.*
import java.util.*

class ShopViewActivity : AppCompatActivity(),View.OnClickListener, ShopAdapter.OnShopSelectedListener {

    private var mFirestore: FirebaseFirestore? = null
    private var mQuery: Query? = null
    private var mShopsRecycler: RecyclerView? = null
    private val mEmptyView: ViewGroup? = null
    private var mAdapter: ShopAdapter? = null
    private var mSearchView: SearchView? = null
    private var Icon_cat: ImageView? = null

    private var bundle: Bundle? = null
    private var query: String? = null
    private var searchTerm: String? = null

    private var title: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        //val toolbar = findViewById<Toolbar>(R.id.tool)
        //setSupportActionBar(toolbar)

        bundle = this.intent.extras
        query = bundle!!.getString("query")

        val category_title = query

        mShopsRecycler = findViewById(R.id.recycler_shops)

        mSearchView = findViewById<SearchView>(R.id.searchViewShop)

        Icon_cat = findViewById<ImageView>(R.id.icon_categoria)

        title = findViewById(R.id.cat_title)

        when (category_title) {
            "Artes Dramáticas" -> Icon_cat!!.setImageResource(R.drawable.ic_teatro)
            "Artes Plásticas" -> Icon_cat!!.setImageResource(R.drawable.ic_artesplasticas)
            "Artesanías" -> Icon_cat!!.setImageResource(R.drawable.ic_artesanias)
            "Danza" -> Icon_cat!!.setImageResource(R.drawable.ic_danza)
            "Literatura" -> Icon_cat!!.setImageResource(R.drawable.ic_literatura)
            "Música" -> Icon_cat!!.setImageResource(R.drawable.ic_musica)
            "Fotografía" -> Icon_cat!!.setImageResource(R.drawable.ic_foto)
            "Video" -> Icon_cat!!.setImageResource(R.drawable.ic_video)
        }

        title!!.text = category_title

        mSearchView!!.isActivated

        initFirestore()
        getFirestoreQuery()
        initRecyclerView()

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

                //mAdapter!!.startListening()
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
                //mAdapter!!.startListening()
                return false
            }
        })


    }

    private fun Search(search: String?): Query? {

        var queryy: String
        if (search!!.length > 1) {
            val text0 = search.substring(0, 1).toUpperCase(Locale.ROOT)
            val text1 = search.substring(1).toLowerCase(Locale.ROOT)

            queryy = text0 + text1
        }else {
            val text0 = search.substring(0, 1).toUpperCase(Locale.ROOT)
            queryy = text0
        }

        mQuery = mFirestore!!.collection("/Negocios").document("$query")
            .collection("Tiendas").orderBy("nombre").startAt(queryy).limit(10)
            //.whereGreaterThanOrEqualTo("nombre", search.toUpperCase(Locale.ROOT))
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
        mQuery = mFirestore!!.collection("/Negocios").document("$query")
            .collection("Tiendas").orderBy("nombre")

    }

    /*
    override fun onPause() {
        super.onPause()
        if (mAdapter != null) {
            mAdapter!!.stopListening()
        }
    }

    override fun onResume() {
        super.onResume()
        if (mAdapter != null) {
            mAdapter!!.startListening()
        }
    }

     */

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

    override fun onClick(v: View) {
        //when (v.id) {
            //R.id.filter_bar -> onFilterClicked()
            //R.id.button_clear_filter -> onClearFilterClicked()
        //}
    }

    override fun onShopSelected(shop: DocumentSnapshot?) {
        val intent = Intent(this, ShopDetailsActivity::class.java).apply {
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

    //initRecyclerView  obtiene la vista de las tiendas según su categoria
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
                Toast.makeText(this@ShopViewActivity, "Error obteniendo los datos", Toast.LENGTH_SHORT).show()

            }
        }
        //mShopsRecycler!!.setLayoutManager(LinearLayoutManager(this)) // vista en forma de lista
        mShopsRecycler!!.layoutManager = GridLayoutManager(this, 2)   //Vista en forma de grilla
        mShopsRecycler!!.adapter = mAdapter
    }
}
