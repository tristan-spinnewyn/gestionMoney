package spinnewyn.project.bank

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import spinnewyn.project.bank.data.model.Account
import spinnewyn.project.bank.data.tier.OperationDAO
import spinnewyn.project.bank.data.tier.PaymentDAO
import spinnewyn.project.bank.data.tier.TiersDAO
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

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val operation = dao.getOperation(account.id_account as Long,position,dateDebut,dateFin)
        val tier = daoTier.getTierById(operation?.fk_id_tier as Long)
        val payment = daoPayment.getPaymentById(operation?.fk_id_payment as Long)
        holder.txtDateTier.setText("${operation?.date_op} - ${tier.tier_name}")
        holder.txtPaiement.setText("${payment.name_payment}")
        holder.txtMontant.setText("${operation.montant}€")
    }
}