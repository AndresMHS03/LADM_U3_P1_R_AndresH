package com.example.ladm_u3_p1_r_andresh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*

enum class ProviderType{
    BASIC
}

class homeActivity : AppCompatActivity() {
    var baseRemota = FirebaseFirestore.getInstance()
    var dataLista  = ArrayList<String>()
    var listaID = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //setup
        val bundle = intent.extras
        val email = bundle!!.getString("email")
        val provider = bundle!!.getString("provider")

        title="Inicio"
        emailtextView.text = email
        providertextView.text = provider

        logOutbutton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        guardar.setOnClickListener {
            insertar()
        }

        recargar()

        listView.setOnItemClickListener { parent, view, position, id ->
            if(listaID.size==0){
                return@setOnItemClickListener
            }

            AlertaEliminarActualizar(position)
        }

    }

    private fun AlertaEliminarActualizar(position: Int) {
        AlertDialog.Builder(this).setTitle("Atención")
            .setMessage("¿Qué deseas hacer con \n${dataLista[position]}?")
            .setPositiveButton("Eliminar"){d,w->
                eliminar(listaID[position])
            }
            .setNegativeButton("Actualizar"){d,w->
                llamarVentanaActualizar(listaID[position])
            }
            .setNeutralButton("Cancelar"){dialog,wich->}
            .show()
    }

    private fun eliminar(idEliminar: String) {
        baseRemota.collection("trabajadores")
            .document(idEliminar)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this,"Se eliminó con exito",Toast.LENGTH_LONG)
                    .show()
                recargar()
            }
            .addOnFailureListener {
                Toast.makeText(this,"No se eliminó",Toast.LENGTH_LONG)
                    .show()
            }
    }

    private fun llamarVentanaActualizar(idActualizar: String) {
        baseRemota.collection("trabajadores")
            .document(idActualizar)
            .get()
            .addOnSuccessListener {
                var v = Intent(this,Main2Activity::class.java)

                v.putExtra("id",idActualizar)
                v.putExtra("nombre",it.getString("nombre"))
                v.putExtra("domicilio",it.getString("domicilio"))
                v.putExtra("carrera",it.getString("carrera"))
                v.putExtra("sueldo",it.get("sueldo").toString())
                v.putExtra("antiguedad",it.get("antiguedad").toString())
                v.putExtra("ingreso",it.get("ingreso").toString())


                startActivity(v)
            }
            .addOnFailureListener {
                Toast.makeText(this,"Error, no hay conexión de red",Toast.LENGTH_LONG)
                    .show()
            }
    }

    private fun recargar() {
        baseRemota.collection("trabajadores")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException != null){
                    //Si es diferente a null si hay error
                    Toast.makeText(this,"Error, no se puede acceder a consulta",Toast.LENGTH_LONG)
                        .show()
                    return@addSnapshotListener
                }
                dataLista.clear()
                listaID.clear()
                for(document in querySnapshot!!){
                    var cadena = document.getString("nombre")+"\n"+document.getString("domicilio")+"\n"+document.getString("carrera")+"\n"+document.get("sueldo")+"\n"+document.get("antiguedad")+" - "+document.get("ingreso")+"\n \n"
                    dataLista.add(cadena)
                    listaID.add(document.id)
                }
                if(dataLista.size==0){
                    dataLista.add("No hay datos")
                }
                var adaptador = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataLista)
                listView.adapter = adaptador
            }
    }

    private fun insertar() {
        var datosInsertar = hashMapOf(
            "nombre" to nombre.text.toString(),
            "domicilio" to domicilio.text.toString(),
            "carrera" to carrera.text.toString(),
            "sueldo" to sueldo.text.toString().toInt(),
            "antiguedad" to antiguedad.text.toString().toInt(),
            "ingreso" to ingreso.text.toString().toString()
        )

        baseRemota.collection("trabajadores")
            .add(datosInsertar)
            .addOnSuccessListener {
                Toast.makeText(this,"Se insertaron los datos", Toast.LENGTH_LONG)
                    .show()
                recargar()
            }
            .addOnFailureListener {
                Toast.makeText(this,"Importante\nNo se pudo insertar", Toast.LENGTH_LONG)
                    .show()
            }
        limpiarCampos()
    }

    private fun limpiarCampos() {
        nombre.setText("")
        domicilio.setText("")
        carrera.setText("")
        sueldo.setText("")
        antiguedad.setText("")
        ingreso.setText("")
    }
}
