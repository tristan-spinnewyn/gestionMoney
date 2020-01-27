package spinnewyn.project.bank

import android.app.DatePickerDialog
import android.os.Bundle
import android.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import spinnewyn.project.bank.data.model.Account
import spinnewyn.project.bank.data.tier.BankDatabase
import java.util.*

class MainActivity : AppCompatActivity() {
    val cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = BankDatabase.getDatabase(this)
        db.seed()
        val account = db.accountDao().getAccount(0)
        //month
        val dateSetListener = object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR,year)
                cal.set(Calendar.MONTH,month)
                cal.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                updateDateInView(account)
            }
        }
        buttonDateAcc.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View) {
                DatePickerDialog(this@MainActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })
        updateDateInView(account)
        //solde
        setTitle(account.nameAccount)
        setSupportActionBar(toolbar)
        val montantSolde = db.operationDao().getSolde(1)+(db.accountDao().getAccount(0)?.soldeInit ?: 0.0)
        val solde = String.format(resources.getString(R.string.soldAccueil), montantSolde)
        soldeAcc.setText(solde)
        if(montantSolde > 0){
            soldeAcc.setTextColor(resources.getColor(R.color.green,null))
        }else{
            soldeAcc.setTextColor(resources.getColor(R.color.red,null))
        }


        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun Date.toEndOfMonth(): Date {
        return Calendar.getInstance().apply {
            time = this@toEndOfMonth
        }.toEndOfMonth().time
    }

    fun Calendar.toEndOfMonth(): Calendar {
        set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
        return this
    }

    fun Date.toFirstOfMonth(): Date{
        return Calendar.getInstance().apply{
            time= this@toFirstOfMonth
        }.toFirstOfMonth().time
    }

    fun Calendar.toFirstOfMonth(): Calendar{
        set(Calendar.DAY_OF_MONTH, getActualMinimum(Calendar.DAY_OF_MONTH))
        return this
    }

    private fun updateDateInView(account: Account){
        val db = BankDatabase.getDatabase(this)
        val currentMonth = cal.getDisplayName(Calendar.MONTH,Calendar.LONG,Locale.getDefault())
        monthAcc.setText(currentMonth)
        val initDate = cal.getTime().toFirstOfMonth()
        val lastDate = initDate.toEndOfMonth()
        val entre = db.operationDao().getEnter(1,initDate,lastDate)
        val txtEntre = String.format(resources.getString(R.string.credAccueil),entre)
        val sortit = db.operationDao().getEnter(1,initDate,lastDate)
        val txtSortit = String.format(resources.getString(R.string.debAccueil),sortit)
        credAcc.setText(txtEntre)
        debAcc.setText(txtSortit)

        lstOp.layoutManager = LinearLayoutManager(this)
        lstOp.adapter = OperationAdapter(db.operationDao(),
            db.paymentDao(),
            db.tierDAO(),
            this,
            account,
            initDate,
            lastDate)
    }
}
