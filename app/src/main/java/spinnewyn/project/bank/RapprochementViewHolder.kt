package spinnewyn.project.bank

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class RapprochementViewHolder(row:View): RecyclerView.ViewHolder(row){
    val txtLblRap = row.findViewById<TextView>(R.id.dateRappTxt)
    val txtStatut = row.findViewById<TextView>(R.id.statutRapp)
    val montantFin = row.findViewById<TextView>(R.id.montantFinRapp)
}