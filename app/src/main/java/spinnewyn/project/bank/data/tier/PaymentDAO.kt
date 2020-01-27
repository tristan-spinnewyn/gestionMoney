package spinnewyn.project.bank.data.tier

import androidx.room.*
import spinnewyn.project.bank.data.model.Payment

@Dao
interface PaymentDAO {
    @Query("SELECT * FROM Payment")
    fun getPayments(): List<Payment>

    @Query("SELECT * from Payment LIMIT 1 offset :position")
    fun getPayment(position: Int): Payment?

    @Query("SELECT count(*) FROM Payment")
    fun countPayment(): Int

    @Insert
    fun insert(payment:Payment): Long

    @Update
    fun update(payment: Payment)

    @Delete
    fun delete(delete: Payment)
}