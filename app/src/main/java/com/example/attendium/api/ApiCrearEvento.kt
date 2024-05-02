import com.example.attendium.data.Evento
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ApiCrearEvento {
    fun crear(evento: Evento) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("eventos")
        val id = myRef.push().key
        id?.let {
            myRef.child(it).setValue(evento)
        }
    }
}