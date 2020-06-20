package com.example.ladm_u3_p1_r_andresh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        title="Autenticación"

        signInbutton.setOnClickListener {
            if(emaileditText.text.isNotEmpty() && passwordeditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emaileditText.text.toString(),
                    passwordeditText.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        mostrarHome(it.result?.user?.email ?:"",ProviderType.BASIC)
                    }else {
                        alerta()
                        alerta2(it.exception.toString())
                    }
                }
            }
        }

        loginbutton.setOnClickListener {
            if(emaileditText.text.isNotEmpty() && passwordeditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emaileditText.text.toString(),
                    passwordeditText.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        mostrarHome(it.result?.user?.email ?:"",ProviderType.BASIC)
                    }else {
                        alerta()
                    }
                }
            }
        }

    }

    fun alerta(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
            .setMessage("Se ha producido un error autenticando al usuario")
            .setPositiveButton("Aceptar",null)
        val dialog : AlertDialog=builder.create()
        dialog.show()
    }

    fun mostrarHome(email : String, provider:ProviderType){
        val homeIntent = Intent(this,homeActivity::class.java).apply {
            putExtra("email",email)
            putExtra("provider",provider.name)
        }
        startActivity(homeIntent)
    }

    fun alerta2(msg : String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
            .setMessage(msg)
            .setPositiveButton("Aceptar",null)
        val dialog : AlertDialog=builder.create()
        dialog.show()
    }
}
