package spinnewyn.project.bank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_rapprochement_liste.*
import spinnewyn.project.bank.data.tier.BankDatabase

class RapprochementListe : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rapprochement_liste)
        val db = BankDatabase.getDatabase(this)

        lstRapp.layoutManager = LinearLayoutManager(this)
        lstRapp.adapter = RapprochementAdapter(db.rapprochementDao(),this)

    }
}
