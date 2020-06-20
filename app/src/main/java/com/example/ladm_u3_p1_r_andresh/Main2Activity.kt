package com.example.ladm_u3_p1_r_andresh

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {
    var id=""
    var baseDatos = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        var extras=intent.extras

                id = extras!!.getString("id").toString()

        nombre.setText(extras.getString("nombre"))
        domicilio.setText(extras.getString("domicilio"))
        carrera.setText(extras.getString("carrera"))
        sueldo.setText(extras.getString("sueldo"))
        antiguedad.setText(extras.getString("antiguedad"))
        ingreso.setText(extras.getString("ingreso"))

        actualizar.setOnClickListener {
            baseDatos.collection("trabajadores")
                .document(id)
                .update("nombre",nombre.text.toString(),"domicilio",domicilio.text.toString(),"carrera",carrera.text.toString(),
                    "sueldo",sueldo.text.toString(),"antiguedad",antiguedad.text.toString(),"ingreso",ingreso.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(this,"Actualizado con exito", Toast.LENGTH_LONG)
                        .show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this,"Error, no se puede actualizar", Toast.LENGTH_LONG)
                        .show()
                }
        }

        regresar.setOnClickListener {
            finish()
        }
    }
}
