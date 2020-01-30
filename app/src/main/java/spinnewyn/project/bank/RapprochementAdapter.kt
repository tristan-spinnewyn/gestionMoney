package spinnewyn.project.bank

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import spinnewyn.project.bank.data.tier.RapprochementDAO
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RapprochementAdapter(private val dao: RapprochementDAO,
                           private val context: Context): RecyclerView.Adapter<RapprochementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RapprochementViewHolder {
        return RapprochementViewHolder(
            LayoutInflater
                .from(context)
                .inflate(R.layout.rapprochement_cell,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return dao.countRapprochement()
    }

    override fun onBindViewHolder(holder: RapprochementViewHolder, position: Int) {
        val rapprochement = dao.getRapprochement(position)
        holder.montantFin.setText(rapprochement.solde.toString())
        if(rapprochement.soldeFinal == null){
            holder.txtStatut.setText("En cours")
        }else{
            holder.txtStatut.setText("Terminer")
        }
        holder.txtLblRap.setText("Rapprochement du ${convertDate(rapprochement.date_rap)}")
    }

    private fun convertDate(date : Date): String{
        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val today: String = formatter.format(date)
        return today
    }
}