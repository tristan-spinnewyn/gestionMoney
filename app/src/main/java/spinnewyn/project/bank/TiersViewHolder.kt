package spinnewyn.project.bank

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TiersViewHolder(row: View) : RecyclerView.ViewHolder(row){
    val lblTier = row.findViewById<TextView>(R.id.lblTierTxt)
    val btnEditTier = row.findViewById<ImageButton>(R.id.editTierBtn)
    val btnDeleteTier = row.findViewById<ImageButton>(R.id.deleteTierBtn)
}