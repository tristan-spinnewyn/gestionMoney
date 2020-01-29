package spinnewyn.project.bank

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import spinnewyn.project.bank.data.model.Account
import spinnewyn.project.bank.data.model.Rapprochement
import spinnewyn.project.bank.data.tier.OperationDAO
import spinnewyn.project.bank.data.tier.PaymentDAO
import spinnewyn.project.bank.data.tier.RapprochementDAO
import spinnewyn.project.bank.data.tier.TiersDAO
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Suppress("UNREACHABLE_CODE")
class OperationRapprochementAdapter(private val daoOpp: OperationDAO,
                                    private val daoPayment: PaymentDAO,
                                    private val daoTier: TiersDAO,
                                    private val daoRapprochement: RapprochementDAO,
                                    private val context: Context,
                                    private val account: Account,
                                    private val rapprochement: Rapprochement): RecyclerView.Adapter<OperationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        return OperationViewHolder(
            LayoutInflater
                .from(context)
                .inflate(R.layout.operation_cell,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return daoOpp.countOperationRapp(account.id_account as Long, rapprochement.id_rapprochement as Long)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val operation = daoOpp.getOperationRapp(account.id_account as Long,position,rapprochement.id_rapprochement as Long)
        val tier = daoTier.getTierById(operation?.fk_id_tier as Long)
        val payment = daoPayment.getPaymentById(operation?.fk_id_payment as Long)
        val date = convertDate(operation.date_op)
        if(operation.statut == 1){
            holder.operationCell.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green))
        }
        holder.txtDateTier.setText("${date} - ${tier.tier_name}")
        holder.txtPaiement.setText(payment.name_payment)
        holder.txtMontant.setText("${operation.montant}€")
        holder.itemView.setOnClickListener{
            if(operation.statut ==1){
                operation.fk_id_rapprochement = null
                operation.statut = null
                daoOpp.update(operation)
            }else{
                operation.fk_id_rapprochement = rapprochement.id_rapprochement
                operation.statut = 1
                daoOpp.update(operation)
            }
            (context as RapprochementActivity).updateRapp()
        }

    }
    private fun convertDate(date : Date): String{
        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val today: String = formatter.format(date)
        return today
    }
}