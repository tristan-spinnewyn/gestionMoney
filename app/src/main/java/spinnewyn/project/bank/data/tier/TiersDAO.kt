package spinnewyn.project.bank.data.tier

import androidx.room.*
import spinnewyn.project.bank.data.model.Tiers

@Dao
interface TiersDAO {
    @Query("SELECT * FROM Tiers")
    fun getTiers(): List<Tiers>

    @Query("SELECT * FROM Tiers limit 1 offset :position")
    fun getTier(position: Int): Tiers

    @Query("SELECT * FROM Tiers where id_tier = :id")
    fun getTierById(id: Long): Tiers

    @Query("SELECT * FROM Tiers where tier_name = :name")
    fun getTierByName(name:String): Tiers?

    @Query("SELECT count(*) FROM Tiers")
    fun countTiers(): Int

    @Update
    fun update(tier: Tiers)

    @Insert
    fun insert(tier: Tiers): Long

    @Delete
    fun delete(tier: Tiers)
}