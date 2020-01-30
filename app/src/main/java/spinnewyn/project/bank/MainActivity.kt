package spinnewyn.project.bank

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import spinnewyn.project.bank.data.model.Account
import spinnewyn.project.bank.data.model.Operation
import spinnewyn.project.bank.data.model.Rapprochement
import spinnewyn.project.bank.data.model.Tiers
import spinnewyn.project.bank.data.tier.BankDatabase
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    val cal = Calendar.getInstance()
    val arrayTouchHelper = arrayOfNulls<ItemTouchHelper>(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lstOp.layoutManager = LinearLayoutManager(this)
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
        updateSolde()
        //solde
        setTitle(account.nameAccount)
        setSupportActionBar(toolbar)



        fab.setOnClickListener {
            val dialog = Dialog(this@MainActivity)
            dialog.setContentView(R.layout.add_operation)
            //spinner
            val spinner = (dialog.findViewById<View>(R.id.lstPayment) as Spinner)
            val payments = db.paymentDao().getPayments()
            val item = arrayOfNulls<String>(payments.size)
            for(i in payments.indices){
                item[i] = payments[i].name_payment
            }
            val arrayAdapter = ArrayAdapter(this@MainActivity,android.R.layout.simple_spinner_item, item)
            spinner.adapter = arrayAdapter
            //autocomplete tier
            val autoComplete = (dialog.findViewById<View>(R.id.edtTiers) as AutoCompleteTextView)
            val tiers = db.tierDAO().getTiers()
            val itemTiers = arrayOfNulls<String>(tiers.size)
            for(i in tiers.indices){
                itemTiers[i] = tiers[i].tier_name
            }
            val arrayAdapterTier = ArrayAdapter(this@MainActivity,android.R.layout.simple_dropdown_item_1line,itemTiers)
            autoComplete.setAdapter(arrayAdapterTier)
            autoComplete.threshold = 1
            //date
            val editText = (dialog.findViewById<View>(R.id.enterDate) as EditText)
            editText.setInputType(InputType.TYPE_NULL)
            editText.setOnClickListener{
                val cldr = Calendar.getInstance()
                val day = cldr[Calendar.DAY_OF_MONTH]
                val month = cldr[Calendar.MONTH]
                val year = cldr[Calendar.YEAR]
                // date picker dialog
                val picker = DatePickerDialog(
                    this@MainActivity,
                    OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        editText.setText(
                            dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                        )
                    }, year, month, day
                )
                picker.show()
            }

            (dialog.findViewById<View>(R.id.btnCancel) as Button)
                .setOnClickListener { dialog.dismiss() }
            (dialog.findViewById<View>(R.id.btnOk) as Button)
                .setOnClickListener{
                    val typeMouv =((dialog.findViewById<View>(R.id.radMouv) as RadioGroup).checkedRadioButtonId
                            == R.id.radSortit)
                    val payment = spinner.selectedItem.toString()
                    val tier = autoComplete.text.toString()
                    val date = ((dialog.findViewById<View>(R.id.enterDate)) as EditText).text.toString()
                    val montant = ((dialog.findViewById<View>(R.id.newMontant)) as EditText).text.toString()

                    if(date.trim().isEmpty() || tier.trim().isEmpty() || montant.trim().isEmpty() ){
                        Toast.makeText(this,
                            "Veuillez remplir tout les champs",
                            Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                    var defMontant = 0.0
                    if(typeMouv) {
                        defMontant -= montant.toDouble()
                    }else{
                        defMontant += montant.toDouble()
                    }
                    val idPayment = db.paymentDao().getPaymentByName(payment)?.id_payment
                    var newTier = db.tierDAO().getTierByName(tier)?.id_tier
                    if(newTier == null){
                        newTier = db.tierDAO().insert(
                            Tiers(
                                tier_name = tier
                            )
                        )
                    }
                    val dateNew = SimpleDateFormat("dd/M/yyyy").parse(date)
                    db.operationDao().insert(Operation(
                        montant = defMontant,
                        date_op = dateNew,
                        fk_id_payment = idPayment,
                        fk_id_tier = newTier,
                        fk_id_account = account.id_account!!
                    ))
                    updateDateInView(account)
                    updateSolde()
                    dialog.dismiss()
                }

            dialog.show()
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
    fun getDebut(time: Date?): Date{
        val cal = Calendar.getInstance()
        cal.setTime(time)
        cal.add(Calendar.MONTH,-1)

        return cal.time
    }

    fun getFin(time: Date): Date{
        val cal = Calendar.getInstance()
        cal.setTime(time)
        cal.add(Calendar.MONTH,1)

        return cal.time
    }

    public fun updateSolde(){
        val db = BankDatabase.getDatabase(this)
        val montantSolde = db.operationDao().getSolde(1)+(db.accountDao().getAccount(0)?.soldeInit ?: 0.0)
        val solde = String.format(resources.getString(R.string.soldAccueil), montantSolde)
        soldeAcc.setText(solde)
        if(montantSolde > 0){
            soldeAcc.setTextColor(resources.getColor(R.color.green,null))
        }else{
            soldeAcc.setTextColor(resources.getColor(R.color.red,null))
        }
    }

    public fun updateDateInView(account: Account){
        if(arrayTouchHelper[0] != null){
            arrayTouchHelper[0]?.attachToRecyclerView(null)
        }
        val db = BankDatabase.getDatabase(this)
        val currentMonth = cal.getDisplayName(Calendar.MONTH,Calendar.LONG,Locale.getDefault())
        monthAcc.setText(currentMonth)
        val initDate = getDebut(cal.getTime()).toEndOfMonth()
        val lastDate = cal.getTime().toEndOfMonth()
        val entre = db.operationDao().getEnter(1,initDate,lastDate)
        val txtEntre = String.format(resources.getString(R.string.credAccueil),entre)
        val sortit = db.operationDao().getExit(1,initDate,lastDate)
        val txtSortit = String.format(resources.getString(R.string.debAccueil),sortit)
        credAcc.setText(txtEntre)
        debAcc.setText(txtSortit)
        lstOp.adapter = OperationAdapter(db.operationDao(),
            db.paymentDao(),
            db.tierDAO(),
            this,
            account,
            initDate,
            lastDate)
        val operationSwipeController =
            OperationSwipeController(
                account,
                initDate,
                lastDate,
                lstOp.adapter as OperationAdapter,
                db.operationDao(),
                this
            )
        val itemTouchHelper= ItemTouchHelper(operationSwipeController)
        itemTouchHelper.attachToRecyclerView(lstOp)
        arrayTouchHelper[0] = itemTouchHelper
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val db = BankDatabase.getDatabase(this)
        return when (item.itemId) {
            R.id.histoRapp -> {
                val intent = Intent(this,RapprochementListe::class.java)
                this.startActivity(intent)
                true
            }
            R.id.rapprochement ->{
                //todo rapprochement
                var lastId = db.rapprochementDao().getMaxID()
                val lastRapp = db.rapprochementDao().getRappById(lastId)
                if(lastRapp.soldeFinal != null){
                    //todo create new rapprochement
                    val dlg = Dialog(this@MainActivity)
                    dlg.setContentView(R.layout.add_rapprochement)
                    (dlg.findViewById<View>(R.id.btnCancel) as Button)
                        .setOnClickListener { dlg.dismiss() }
                    (dlg.findViewById<View>(R.id.btnOk) as Button)
                        .setOnClickListener{
                            val solde = (dlg.findViewById<View>(R.id.soldeInit) as EditText).text.toString()
                            if(solde.trim().isEmpty()){
                                Toast.makeText(this,
                                    "Veuillez saisir le solde du relevé",
                                    Toast.LENGTH_LONG).show()
                                return@setOnClickListener
                            }else{
                                lastId = db.rapprochementDao().insert(
                                    spinnewyn.project.bank.data.model.Rapprochement(
                                        date_rap = Calendar.getInstance().time,
                                        solde = (dlg.findViewById<View>(R.id.soldeInit) as EditText).text.toString().toDouble()
                                    )
                                )
                                val intent = Intent(this,RapprochementActivity::class.java)
                                val defRapp = db.rapprochementDao().getRappById(lastId)
                                dlg.dismiss()
                                intent.putExtra("rapprochement",defRapp)
                                this.startActivity(intent)

                            }
                        }
                    dlg.show()
                }else{
                    val intent = Intent(this,RapprochementActivity::class.java)
                    val defRapp = db.rapprochementDao().getRappById(lastId)
                    intent.putExtra("rapprochement",defRapp)
                    this.startActivity(intent)
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        val db = BankDatabase.getDatabase(this)
        val account = db.accountDao().getAccount(0)
        updateDateInView(account)
        super.onResume()
    }

    override fun onRestart() {
        val db = BankDatabase.getDatabase(this)
        val account = db.accountDao().getAccount(0)
        updateDateInView(account)
        super.onRestart()
    }
}
