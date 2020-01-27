package spinnewyn.project.bank

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OperationViewHolder(row: View) : RecyclerView.ViewHolder(row) {
    val txtDateTier = row.findViewById<TextView>(R.id.dateTierTxt)
    val txtPaiement = row.findViewById<TextView>(R.id.paymentTxt)
    val txtMontant = row.findViewById<TextView>(R.id.montantOpTxt)
}