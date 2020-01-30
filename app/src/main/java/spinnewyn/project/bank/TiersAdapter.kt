package spinnewyn.project.bank

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import spinnewyn.project.bank.data.tier.OperationDAO
import spinnewyn.project.bank.data.tier.TiersDAO

class TiersAdapter(
    private val dao: TiersDAO,
    private val oppDao: OperationDAO,
    private val context: Context
) : RecyclerView.Adapter<TiersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiersViewHolder {
        return TiersViewHolder(
            LayoutInflater
                .from(context)
                .inflate(R.layout.tiers_cell, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dao.countTiers()
    }

    override fun onBindViewHolder(holder: TiersViewHolder, position: Int) {
        val tier = dao.getTier(position)
        holder.lblTier.setText(tier.tier_name)
        holder.btnDeleteTier.setOnClickListener {
            if (oppDao.getNbOpInTier(tier.id_tier!!) != 0) {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Vous ne pouvez pas supprimer un tier utilisé.")
                    .setPositiveButton(R.string.add,
                        DialogInterface.OnClickListener { dialog, _ ->
                            dialog.dismiss()
                        })
                builder.create().show()
            } else {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Etes vous sur de vouloir supprimer ce tier ?")
                    .setNegativeButton(R.string.cancel,
                        DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
                    .setPositiveButton(R.string.add,
                        DialogInterface.OnClickListener { dialog, _ ->
                            dao.delete(tier)
                            notifyDataSetChanged()
                            dialog.dismiss()
                        })
                builder.create().show()
            }
        }
        holder.btnEditTier.setOnClickListener{
            val dlg = Dialog(context)
            dlg.setContentView(R.layout.add_tiers)
            (dlg.findViewById<View>(R.id.lblTier) as EditText).setText(tier.tier_name)
            (dlg.findViewById<View>(R.id.btnCancel) as Button)
                .setOnClickListener{dlg.dismiss()}
            (dlg.findViewById<View>(R.id.btnOk) as Button)
                .setOnClickListener{
                    val nameTier = (dlg.findViewById<View>(R.id.lblTier) as EditText).text.toString()
                    tier.tier_name = nameTier
                    dao.update(tier)
                    notifyDataSetChanged()
                    dlg.dismiss()
                }

            dlg.show()
        }

    }
}