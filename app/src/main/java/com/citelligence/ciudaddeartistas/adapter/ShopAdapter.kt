package com.citelligence.ciudaddeartistas.adapter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.citelligence.ciudaddeartistas.R
import com.citelligence.ciudaddeartistas.model.Shop
import com.citelligence.ciudaddeartistas.util.GlideApp
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import java.net.URLEncoder

open class ShopAdapter(mquery: Query?, mListener: OnShopSelectedListener) : FirestoreAdapter<ShopAdapter.ViewHolder>() {

    interface OnShopSelectedListener { fun onShopSelected(shop: DocumentSnapshot?) }

    private var mListener: OnShopSelectedListener? = null

    init{
        FirestoreAdapter(mquery)
        this.mListener = mListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_shop, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position), mListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView? = null
        var nameView: TextView? = null
        var descView: TextView? = null
        var btn_fb: FloatingActionButton? = null
        var btn_wh: FloatingActionButton? = null
        var btn_it: FloatingActionButton? = null
        var btn_yt: FloatingActionButton? = null

        init {
            imageView = itemView.findViewById(R.id.shop_item_image)
            nameView = itemView.findViewById(R.id.shop_item_nombre)
            descView = itemView.findViewById(R.id.shop_item_descripcion)
            btn_fb = itemView.findViewById(R.id.FB)
            btn_wh = itemView.findViewById(R.id.WHS)
            btn_it = itemView.findViewById(R.id.IN)
            btn_yt = itemView.findViewById(R.id.YT)
        }

        fun bind(snapshot: DocumentSnapshot?, listener: OnShopSelectedListener?) {

            val shop: Shop? = snapshot?.toObject(Shop::class.java)

            val storageReference = FirebaseStorage.getInstance()

            val imgReference = storageReference.getReferenceFromUrl("gs://ciudad-de-artistas.appspot.com/Logos_tiendas/${shop?.getLogo()}")

            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(20))

            imageView?.let {
                GlideApp.with(itemView.context)
                    .load(imgReference)
                    .apply(requestOptions)
                    .into(it)
            }

            //imageView!!.setImageResource(R.drawable.ic_launcher_rapicarmen_foreground)
            nameView!!.text = shop?.getNombre()
            descView!!.text = shop?.getDescripcion()



            btn_fb!!.setOnClickListener {
                if (shop?.getFB() == "" || shop?.getFB() == null){
                    Snackbar.make(itemView, "No disponible", Snackbar.LENGTH_LONG).setAction("Action", null).show()
                }else {
                    try {
                        val facebookIntent = Intent(Intent.ACTION_VIEW)
                        val facebookUrl = "fb://facewebmodal/f?href=${shop.getFB()}"
                        facebookIntent.data = Uri.parse(facebookUrl)
                        itemView.context.startActivity(facebookIntent)
                    }catch (e: ActivityNotFoundException){
                        showAlert("Algo salió mal o el enlace no esta disponible")
                    }

                }
            }

            btn_wh!!.setOnClickListener {
                if (snapshot!!["celular"] == "" || snapshot["celular"] == null ){
                    Snackbar.make(itemView, "No disponible", Snackbar.LENGTH_LONG).setAction("Action", null).show()
                }else {
                    val code = "+57"
                    val contact = code + snapshot["celular"]
                    val message = "Estoy interesado en adquirir sus productos o servicios"

                    val url = "https://api.whatsapp.com/send?phone=$contact"+"&text=" + URLEncoder.encode(message, "UTF-8")
                    try {
                        val pm = itemView.context.packageManager
                        pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(url)
                        itemView.context.startActivity(i)
                    } catch (e: PackageManager.NameNotFoundException) {
                        snapshot["celular"]
                    }
                }
            }

            btn_it!!.setOnClickListener {
                if (snapshot!!["ins"] == "" || snapshot["ins"] == null){
                    Snackbar.make(itemView, "No disponible", Snackbar.LENGTH_LONG).setAction("Action", null).show()
                }else {
                    val uri = Uri.parse(snapshot["ins"].toString())
                    val likeIng = Intent(Intent.ACTION_VIEW, uri)

                    likeIng.setPackage("com.instagram.android")

                    try {
                        itemView.context.startActivity(likeIng)
                    } catch (e: ActivityNotFoundException) {
                        showAlert("Algo salió mal o el enlace no esta disponible")
                    }
                }
            }

            btn_yt!!.setOnClickListener {
                if (shop?.getYT() == "" || shop?.getYT() == null){
                    Snackbar.make(itemView, "No disponible", Snackbar.LENGTH_LONG).setAction("Action", null).show()
                }else {
                    var intent: Intent?
                    val uri = Uri.parse(shop.getYT())
                    try {
                        intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.setPackage("com.google.android.youtube")
                        itemView.context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        showAlert("Algo salió mal o el enlace no esta disponible")
                    }
                }
            }

            // Click listener
            itemView.setOnClickListener {
                listener?.onShopSelected(snapshot)
            }
        }

        fun showAlert(msg: String) {
            val builder = AlertDialog.Builder(itemView.context)
            builder.setTitle("Ups!")
            builder.setMessage(msg)
            builder.setPositiveButton("Aceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

    }
}