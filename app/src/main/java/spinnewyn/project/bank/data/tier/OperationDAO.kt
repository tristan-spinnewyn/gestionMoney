package spinnewyn.project.bank.data.tier

import androidx.room.*
import spinnewyn.project.bank.data.model.Operation
import java.util.*

@Dao
interface OperationDAO {
    @Query("SELECT * FROM Operation WHERE fk_id_account = :accountId and date_op BETWEEN :dateInit and :dateFin order by date_op DESC, id_op")
    fun getOperations(accountId: Long, dateInit: Date, dateFin: Date): List<Operation>

    @Query("SELECT * FROM Operation where fk_id_account = :accountId and date_op BETWEEN :dateInit and :dateFin order by date_op DESC, id_op LIMIT 1 offset :position")
    fun getOperation(accountId: Long, position: Int, dateInit: Date, dateFin: Date): Operation?

    @Query("SELECT count(*) FROM Operation where fk_id_account = :accountId and date_op BETWEEN :dateInit and :dateFin order by date_op DESC, id_op")
    fun countOperation(accountId: Long, dateInit: Date, dateFin: Date): Int

    @Query("UPDATE Operation SET fk_id_rapprochement = :idRapprochement where date_op BETWEEN :dateInit and :dateFin")
    fun startRapprochement(idRapprochement: Long, dateInit: Date, dateFin: Date)

    @Query("SELECT sum(montant) FROM Operation where fk_id_account = :idAcc group by fk_id_account ")
    fun getSolde(idAcc : Long): Double

    @Query("SELECT sum(montant) FROM Operation where montant > 0 and fk_id_account = :idAcc and date_op BETWEEN :dateInit and :dateFin group by fk_id_account")
    fun getEnter(idAcc : Long, dateInit: Date, dateFin: Date): Double

    @Query("SELECT sum(montant) FROM Operation where montant < 0 and fk_id_account = :idAcc and date_op BETWEEN :dateInit and :dateFin group by fk_id_account")
    fun getExit(idAcc : Long, dateInit: Date, dateFin: Date): Double

    @Query("SELECT count(*) FROM Operation where fk_id_account = :accountId and (fk_id_rapprochement is null or fk_id_rapprochement = :rappId)")
    fun countOperationRapp(accountId: Long, rappId: Long): Int

    @Query("SELECT * FROM Operation where fk_id_account = :accountId and (fk_id_rapprochement is null or fk_id_rapprochement =:rappId) order by date_op ASC, id_op LIMIT 1 offset :position")
    fun getOperationRapp(accountId: Long,position:Int, rappId: Long) : Operation?

    @Query("SELECT sum(montant) FROM Operation where fk_id_account = :accountId and fk_id_rapprochement= :rappId")
    fun getSoldeRapp(accountId: Long, rappId: Long): Double

    @Query("UPDATE Operation set statut = 2 where fk_id_account = :accountId and fk_id_rapprochement= :rappId")
    fun updateRappDef(accountId: Long, rappId: Long)

    @Query("SELECT count(*) FROM Operation where fk_id_tier = :idTier")
    fun getNbOpInTier(idTier: Long): Int

    @Insert
    fun insert(operation: Operation): Long

    @Update
    fun update(operation: Operation)

    @Delete
    fun delete(operation: Operation)

    @Query("SELECT * FROM Operation where id_op = 1")
    fun test(): Operation

}