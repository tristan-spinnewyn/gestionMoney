package spinnewyn.project.bank

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import spinnewyn.project.bank.data.model.Account
import spinnewyn.project.bank.data.model.Tiers
import spinnewyn.project.bank.data.tier.OperationDAO
import spinnewyn.project.bank.data.tier.PaymentDAO
import spinnewyn.project.bank.data.tier.TiersDAO
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class OperationAdapter(private val dao: OperationDAO,
                       private val daoPayment: PaymentDAO,
                       private val daoTier: TiersDAO,
                       private val context: Context,
                       private val account: Account,
                       private val dateDebut: Date,
                       private val dateFin: Date): RecyclerView.Adapter<OperationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        return OperationViewHolder(
            LayoutInflater
                .from(context)
                .inflate(R.layout.operation_cell,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return dao.countOperation(account.id_account as Long,dateDebut,dateFin)
    }

    @SuppressLint("WrongViewCast")
    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val operation = dao.getOperation(account.id_account as Long,position,dateDebut,dateFin)
        val tier = daoTier.getTierById(operation?.fk_id_tier as Long)
        val payment = daoPayment.getPaymentById(operation?.fk_id_payment as Long)
        val date = convertDate(operation?.date_op)
        holder.txtDateTier.setText("${date} - ${tier.tier_name}")
        holder.txtPaiement.setText("${payment.name_payment}")
        holder.txtMontant.setText("${operation.montant}€")
        holder.itemView.setOnClickListener{
            /*
            Date
             */
            val dlg = Dialog(context)
            dlg.setContentView(R.layout.add_operation)
            val editText = (dlg.findViewById<View>(R.id.enterDate) as EditText)
            val cal = Calendar.getInstance()
            cal.time = operation.date_op
            val day = cal[Calendar.DAY_OF_MONTH]
            val month = cal[Calendar.MONTH]
            val year = cal[Calendar.YEAR]
            val tier = daoTier.getTierById(operation.fk_id_tier!!)
            val payment = daoPayment.getPaymentById(operation.fk_id_payment!!)
            editText.setInputType(InputType.TYPE_NULL)
            editText.setText("$day/${month+1}/$year")
            editText.setOnClickListener{
                val picker = DatePickerDialog(
                    context,
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        editText.setText(
                            dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                        )
                    }, year, month, day
                )
                picker.show()
            }
            /*
            autocomplete tier
             */
            val autocomplete = (dlg.findViewById<View>(R.id.edtTiers) as AutoCompleteTextView)
            val tiers = daoTier.getTiers()
            val itemTiers = arrayOfNulls<String>(tiers.size)
            for(i in tiers.indices){
                itemTiers[i] = tiers[i].tier_name
            }
            val arrayAdapterTier = ArrayAdapter(context,android.R.layout.simple_dropdown_item_1line,itemTiers)
            autocomplete.setAdapter(arrayAdapterTier)
            autocomplete.threshold = 1
            autocomplete.setText(tier.tier_name)

            /*
            spinner
             */
            val spinner = (dlg.findViewById<View>(R.id.lstPayment) as Spinner)
            val payments = daoPayment.getPayments()
            val item = arrayOfNulls<String>(payments.size)
            for(i in payments.indices){
                item[i] = payments[i].name_payment
            }
            val arrayAdapter = ArrayAdapter(context,android.R.layout.simple_spinner_item,item)
            spinner.adapter = arrayAdapter
            spinner.setSelection(payment.id_payment!!.toInt() - 1)
            var montantInit = 0.0
            if(operation.montant <0){
                ((dlg.findViewById<View>(R.id.radMouv) as RadioGroup).check(R.id.radSortit))
                montantInit = operation.montant * -1
            }else{
                ((dlg.findViewById<View>(R.id.radMouv) as RadioGroup).check(R.id.radEntre))
                montantInit = operation.montant
            }

            (dlg.findViewById<View>(R.id.newMontant) as EditText).setText(montantInit.toString())
            (dlg.findViewById<View>(R.id.btnCancel) as Button)
                .setOnClickListener{dlg.dismiss()}
            (dlg.findViewById<View>(R.id.btnOk) as Button)
                .setOnClickListener{
                    val typeMouv =((dlg.findViewById<View>(R.id.radMouv) as RadioGroup).checkedRadioButtonId
                            == R.id.radSortit)
                    val payment = spinner.selectedItem.toString()
                    val tier = autocomplete.text.toString()
                    val date = ((dlg.findViewById<View>(R.id.enterDate)) as EditText).text.toString()
                    val montant = ((dlg.findViewById<View>(R.id.newMontant)) as EditText).text.toString()

                    if(date.trim().isEmpty() || tier.trim().isEmpty() || montant.trim().isEmpty() ){
                        Toast.makeText(context,
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
                    val idPayment = daoPayment.getPaymentByName(payment)?.id_payment
                    var newTier = daoTier.getTierByName(tier)?.id_tier
                    if(newTier == null){
                        newTier = daoTier.insert(
                            Tiers(
                                tier_name = tier
                            )
                        )
                    }
                    val dateNew = SimpleDateFormat("dd/M/yyyy").parse(date)
                    operation.montant = defMontant
                    operation.date_op = dateNew
                    operation.fk_id_payment = idPayment
                    operation.fk_id_tier = newTier
                    dao.update(operation)
                    (context as MainActivity).updateDateInView(account)
                    (context).updateSolde()
                    dlg.dismiss()
                }
            dlg.show()
        }

    }
    private fun convertDate(date : Date): String{
        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val today: String = formatter.format(date)
        return today
    }
}