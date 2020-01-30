package spinnewyn.project.bank

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_rapprochement_liste.*

import kotlinx.android.synthetic.main.activity_tiers.*
import kotlinx.android.synthetic.main.content_tiers.*
import spinnewyn.project.bank.data.model.Tiers
import spinnewyn.project.bank.data.tier.BankDatabase

class TiersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tiers)
        setSupportActionBar(toolbar)

        val db = BankDatabase.getDatabase(this)
        setTitle("Gestion des tiers")
        lstTier.layoutManager = LinearLayoutManager(this)
        lstTier.adapter = TiersAdapter(db.tierDAO(),db.operationDao(),this)

        fab.setOnClickListener {
            val dialog = Dialog(this@TiersActivity)

            dialog.setContentView(R.layout.add_tiers)
            (dialog.findViewById<View>(R.id.btnCancel) as Button)
                .setOnClickListener{dialog.dismiss()}
            (dialog.findViewById<View>(R.id.btnOk) as Button)
                .setOnClickListener{
                    val nameTier = (dialog.findViewById<View>(R.id.lblTier) as EditText).text.toString()
                    db.tierDAO().insert(Tiers(
                        tier_name = nameTier
                    ))
                    (lstTier.adapter as TiersAdapter).notifyDataSetChanged()
                    dialog.dismiss()
                }
            dialog.show()
        }
    }

}
