package spinnewyn.project.bank

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.activity_rapprochement.*
import kotlinx.android.synthetic.main.content_rapprochement.*
import spinnewyn.project.bank.data.model.Rapprochement
import spinnewyn.project.bank.data.tier.BankDatabase

class RapprochementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rapprochement)
        setSupportActionBar(toolbar)

        lstOpRapp.layoutManager = LinearLayoutManager(this)
        val db = BankDatabase.getDatabase(this)
        val account = db.accountDao().getAccount(0)
        val rapp = intent.getSerializableExtra("rapprochement") as Rapprochement

        val soldeAtt = rapp.solde
        val txtSoldeAtt = String.format(resources.getString(R.string.soldeAtt),soldeAtt)
        soldeAcc.setText(txtSoldeAtt)
        updateRapp()


        valRapp.setOnClickListener{
            val oldRapp = db.rapprochementDao().getRappById((rapp.id_rapprochement?.minus(1)) as Long)
            var oldMontant = oldRapp.soldeFinal
            val soldeActuel = db.operationDao().getSoldeRapp(account.id_account as Long,rapp.id_rapprochement as Long) + oldMontant!!
            if(soldeActuel != soldeAtt){
                val dlg = AlertDialog.Builder(this)
                    .setTitle("Le solde doit etre égale avec le solde à atteindre")
                    .setPositiveButton(R.string.add) { dialog, which -> dialog.dismiss()
                    }
                    .show()
            }else{
                rapp.soldeFinal = soldeAtt
                db.rapprochementDao().update(rapp)
                db.operationDao().updateRappDef(account.id_account!!,rapp.id_rapprochement!!)
                this.finish()
            }
        }
    }

    fun updateRapp(){
        val db = BankDatabase.getDatabase(this)
        val account = db.accountDao().getAccount(0)
        val rapp = intent.getSerializableExtra("rapprochement") as Rapprochement
        val oldRapp = db.rapprochementDao().getRappById((rapp.id_rapprochement?.minus(1)) as Long)
        var oldMontant = oldRapp.soldeFinal
        val soldeActuel = db.operationDao().getSoldeRapp(account.id_account as Long,rapp.id_rapprochement as Long) + oldMontant!!
        val txtSoldeActuel = String.format(resources.getString(R.string.actSolde),soldeActuel)
        debAcc.setText(txtSoldeActuel)
        lstOpRapp.adapter = OperationRapprochementAdapter(db.operationDao(),
            db.paymentDao(),
            db.tierDAO(),
            db.rapprochementDao(),
            this,
            account,
            rapp
            )
    }

}
