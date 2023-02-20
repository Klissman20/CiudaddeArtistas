package com.citelligence.ciudaddeartistas

import android.content.ActivityNotFoundException
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.citelligence.ciudaddeartistas.util.GlideApp
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.net.URLEncoder


class ShopDetailsActivity : AppCompatActivity() {

    val storageReference = FirebaseStorage.getInstance()
    var FACEBOOK_URL: String? = null
    var FACEBOOK_PAGE_ID: String? = null
    var WHATSAPP_ID: String? = null
    var INSTAGRAM_ID: String? = null
    var YOUTUBE_ID: String? = null
    var youTubePlayerView: YouTubePlayerView? = null
    var Video: String? = null

    private var Icon_cat: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_detail)

        getData()

        youTubePlayerView = findViewById(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView!!)

        val bundle: Bundle? = this.intent.extras
        Video = bundle!!.getString("video")

        youTubePlayerView!!.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = Video
                youTubePlayer.loadVideo(videoId!!, 0f)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        youTubePlayerView!!.release()
    }

    private fun getData() {

        val bundle: Bundle? = this.intent.extras
        val nombre = bundle!!.getString("nombre")
        val telefono = bundle.getString("tel")
        val descripcion = bundle.getString("descripcion")
        val logo = bundle.getString("logo")
        val productos = bundle.getString("productos")
        val direccion = bundle.getString("direccion")
        val contacto = bundle.getString("contacto")
        val celular = bundle.getString("celular")
        val mail = bundle.getString("mail")
        val web = bundle.getString("web")
        val fb = bundle.getString("fb")
        val ins = bundle.getString("ins")
        val yt = bundle.getString("yt")
        val categoria = bundle.getString("categoria")

        FACEBOOK_URL = fb
        FACEBOOK_PAGE_ID = nombre
        WHATSAPP_ID = celular
        INSTAGRAM_ID = ins
        YOUTUBE_ID = yt

        val imgReference = storageReference.getReferenceFromUrl("gs://ciudad-de-artistas.appspot.com/Logos_tiendas/$logo")
        //val imgReference = storageReference.reference.child("ImagenesTiendas/$nombre.png")

        val imageView = findViewById<ImageView>(R.id.shop_item_imageImg)

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(50))

        GlideApp.with(this)
            .load(imgReference)
            .apply(requestOptions)
            .into(imageView)

        val nameView = findViewById<TextView>(R.id.nombredetail)
        val telView = findViewById<TextView>(R.id.telefonodetail)
        val descView = findViewById<TextView>(R.id.descrip)
        val prodView = findViewById<TextView>(R.id.prodetail)
        val dirView = findViewById<TextView>(R.id.addressdetail)
        val contactoView = findViewById<TextView>(R.id.contactodetail)
        val celularView = findViewById<TextView>(R.id.celdetail)
        val mailView = findViewById<TextView>(R.id.maildetail)
        val webView = findViewById<TextView>(R.id.webdetail)

        Icon_cat = findViewById<ImageView>(R.id.icon_categoria)

        when (categoria) {
            "Artes Dramáticas" -> Icon_cat!!.setImageResource(R.drawable.ic_teatro_blue)
            "Artes Plásticas" -> Icon_cat!!.setImageResource(R.drawable.ic_artesplasticas_blue)
            "Artesanías" -> Icon_cat!!.setImageResource(R.drawable.ic_artesanias_blue)
            "Danza" -> Icon_cat!!.setImageResource(R.drawable.ic_danza_blue)
            "Literatura" -> Icon_cat!!.setImageResource(R.drawable.ic_literatura_blue)
            "Música" -> Icon_cat!!.setImageResource(R.drawable.ic_musica_blue)
            "Fotografía" -> Icon_cat!!.setImageResource(R.drawable.ic_foto_blue)
            "Video" -> Icon_cat!!.setImageResource(R.drawable.ic_video_blue)
        }

        nameView.text = nombre
        telView.text = telefono
        descView.text = descripcion
        prodView.text = Html.fromHtml(productos)
        dirView.text = direccion
        contactoView.text = contacto
        celularView.text = celular
        mailView.text = mail
        webView.text = web

        telView.paintFlags = telView.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        telView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val cm: ClipboardManager =
                    baseContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.text = (telView.getText())
                Toast.makeText(baseContext, "Teléfono copiado", Toast.LENGTH_SHORT).show()
            }
        })

        celularView.paintFlags = celularView.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        celularView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val cm: ClipboardManager =
                    baseContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.text = (telView.getText())
                Toast.makeText(baseContext, "Celular copiado", Toast.LENGTH_SHORT).show()
            }
        })

        dirView.paintFlags = dirView.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        dirView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val mapUri = Uri.parse("geo:0,0?q=$direccion")
                val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        })

        val fab: View = findViewById(R.id.FB)
        val whats: View = findViewById(R.id.WHS)
        val insta: View = findViewById(R.id.IN)
        val youtube: View = findViewById(R.id.YT)

        fab.setOnClickListener { view ->
            if (fb == "" || fb == null){
                Snackbar.make(view, "No disponible", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }else {
                try {
                    val facebookIntent = Intent(Intent.ACTION_VIEW)
                    val facebookUrl = getFacebookPageURL(this)
                    facebookIntent.data = Uri.parse(facebookUrl)
                    startActivity(facebookIntent)
                }catch (e: ActivityNotFoundException){
            showAlert("Algo salió mal o el enlace no esta disponible")
        }
            }

        }

        whats.setOnClickListener { view ->
            if (celular == "" || celular == null ){
                Snackbar.make(view, "No disponible", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }else {
                getWhatsappURL(this)
            }
        }

        insta.setOnClickListener { view ->
            if (ins == "" || ins == null){
                Snackbar.make(view, "No disponible", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }else {
                getInstagramURL()
            }
        }

        youtube.setOnClickListener { view ->
            if (yt == "" || yt == null){
                Snackbar.make(view, "No disponible", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }else {
                getYoutubeURL()
            }
        }
    }

    private fun getFacebookPageURL(context: Context): String? {
        val packageManager = context.packageManager
        return try {
            val versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode
            if (versionCode >= 3002850) { //versiones nuevas de facebook
                "fb://facewebmodal/f?href=$FACEBOOK_URL"
            } else { //versiones antiguas de fb
                "fb://page/$FACEBOOK_PAGE_ID"
            }
        } catch (e: PackageManager.NameNotFoundException) {
            FACEBOOK_URL //normal web url
        }
    }

    private fun getWhatsappURL(context: Context){
        val code = "+57"
        val contact = code + WHATSAPP_ID
        val message = "Hola! ¿ Cómo  estás? , revisé tu perfil en la App Ciudad de Artistas y quiero conocer más de tu trabajo artístico"

        val url = "https://api.whatsapp.com/send?phone=$contact"+"&text=" + URLEncoder.encode(message, "UTF-8")
        try {
            val pm = context.packageManager
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        } catch (e: PackageManager.NameNotFoundException) {
            showAlert("Algo salió mal o el enlace no esta disponible")
        }
    }

    private fun getInstagramURL(){
        val uri = Uri.parse(INSTAGRAM_ID)
        val likeIng = Intent(Intent.ACTION_VIEW, uri)

        likeIng.setPackage("com.instagram.android")

        try {
            startActivity(likeIng)
        } catch (e: ActivityNotFoundException) {
            showAlert("Algo salió mal o el enlace no esta disponible")
        }
    }

    private fun getYoutubeURL(){
        var intent: Intent?
        val uri = Uri.parse(YOUTUBE_ID)
        try {
            intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.youtube")
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            showAlert("Algo salió mal o el enlace no esta disponible")
        }
    }

    private fun showAlert(msg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Vaya!")
        builder.setMessage(msg)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}