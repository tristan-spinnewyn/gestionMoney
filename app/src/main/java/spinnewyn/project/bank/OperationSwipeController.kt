package spinnewyn.project.bank

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import spinnewyn.project.bank.data.tier.OperationDAO
import java.util.*

class OperationSwipeController(private val account: spinnewyn.project.bank.data.model.Account,
                               private val dateDebut: Date,
                               private val dateFin: Date,
                               private val operationAdapter: OperationAdapter,
                               private val dao : OperationDAO,
                               private val context: Context):ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0, ItemTouchHelper.END)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val operation = dao.getOperation(account.id_account!!,viewHolder.adapterPosition,dateDebut,dateFin) ?: return
        val dlg = AlertDialog.Builder(context)
        dlg.setMessage("Etes vous sur de vouloir supprimer l'opération ?")
            .setPositiveButton(
                R.string.add,
                DialogInterface.OnClickListener{
                    dialog, id ->
                        dao.delete(operation)
                        (context as MainActivity).updateDateInView(account)
                        (context).updateSolde()
                }
            )
            .setNegativeButton(
                R.string.cancel,
                DialogInterface.OnClickListener{
                    dialog,id->
                        operationAdapter.notifyDataSetChanged()
                        (context as MainActivity).updateDateInView(account)
                        dialog.dismiss()
                }
            )
            .setOnCancelListener(
                DialogInterface.OnCancelListener { dialog -> operationAdapter.notifyDataSetChanged() }
            )
            dlg.create().show()

    }

}