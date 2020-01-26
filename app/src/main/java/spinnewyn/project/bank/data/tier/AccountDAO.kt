package spinnewyn.project.bank.data.tier

import androidx.room.*
import spinnewyn.project.bank.data.model.Account

@Dao
interface AccountDAO {
    @Query("SELECT * FROM Account")
    fun getAccounts(): List<Account>

    @Query("SELECT * FROM Account LIMIT 1 OFFSET :position")
    fun getAccount(position: Int): Account?

    @Query("SELECT count(*) FROM Account")
    fun countAccount(): Int

    @Insert
    fun insert(acc: Account): Long

    @Update
    fun update(acc:Account)

    @Delete
    fun delete(acc:Account)
}