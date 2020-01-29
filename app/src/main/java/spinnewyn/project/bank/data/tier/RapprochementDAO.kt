package spinnewyn.project.bank.data.tier

import androidx.room.*
import spinnewyn.project.bank.data.model.Rapprochement

@Dao
interface RapprochementDAO {
    @Query("SELECT * FROM Rapprochement")
    fun getRapprochements(): List<Rapprochement>

    @Query("SELECT * FROM Rapprochement LIMIT 1 offset :position")
    fun getRapprochement(position: Int): Rapprochement

    @Query("SELECT count(*) FROM Rapprochement")
    fun countRapprochement(): Int

    @Query("SELECT Max(id_rapprochement) from Rapprochement")
    fun getMaxID(): Long

    @Query("SELECT * FROM Rapprochement where id_rapprochement = :id")
    fun getRappById(id: Long): Rapprochement

    @Insert
    fun insert(rapprochement: Rapprochement): Long

    @Update
    fun update(rapprochement: Rapprochement)

    @Delete
    fun delete(rapprochement: Rapprochement)
}