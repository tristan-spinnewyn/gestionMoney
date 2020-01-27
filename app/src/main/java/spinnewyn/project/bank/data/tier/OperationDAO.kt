package spinnewyn.project.bank.data.tier

import androidx.room.*
import spinnewyn.project.bank.data.model.Operation
import java.util.*

@Dao
interface OperationDAO {
    @Query("SELECT * FROM Operation WHERE fk_id_account = :accountId")
    fun getOperations(accountId: Long): List<Operation>

    @Query("SELECT * FROM Operation where fk_id_account = :accountId LIMIT 1 offset :position")
    fun getOperation(accountId: Long, position: Int): Operation?

    @Query("SELECT count(*) FROM Operation where fk_id_account = :accountId")
    fun countOperation(accountId: Long): Int

    @Query("UPDATE Operation SET fk_id_rapprochement = :idRapprochement where date_op BETWEEN :dateInit and :dateFin")
    fun startRapprochement(idRapprochement: Long, dateInit: Date, dateFin: Date)

    @Insert
    fun insert(operation: Operation): Long

    @Update
    fun update(operation: Operation)

    @Delete
    fun delete(operation: Operation)

}