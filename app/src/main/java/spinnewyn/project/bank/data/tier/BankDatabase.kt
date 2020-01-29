package spinnewyn.project.bank.data.tier

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import spinnewyn.project.bank.data.model.*
import java.text.ParseException
import java.util.*

@Database(version= 1,entities = [Tiers::class, Payment::class, Account::class, Rapprochement::class, Operation::class])
@TypeConverters(DateConverter::class)
abstract class BankDatabase : RoomDatabase() {
    abstract fun accountDao() : AccountDAO
    abstract fun operationDao() : OperationDAO
    abstract fun paymentDao(): PaymentDAO
    abstract fun rapprochementDao(): RapprochementDAO
    abstract fun tierDAO(): TiersDAO

    fun seed(){
        if(accountDao().countAccount() == 0){
            try {
                val account = Account(
                    nameAccount = "Mon compte"
                )
                val idAcc = accountDao().insert(account)

                val pay1 = Payment(
                    name_payment = "Virement"
                )
                val pay2 = Payment(
                    name_payment = "Retrait"
                )
                val pay3 = Payment(
                    name_payment = "Carte bleu"
                )
                val pay4 = Payment(
                    name_payment = "Chèque"
                )
                val pay5 = Payment(
                    name_payment = "Prélèvement"
                )
                val idPay1 = paymentDao().insert(pay1)
                val idPay2 = paymentDao().insert(pay2)
                val idPay3 = paymentDao().insert(pay3)
                val idPay4 = paymentDao().insert(pay4)
                val idPay5 = paymentDao().insert(pay5)
                val tier = Tiers(
                    tier_name = "Auchan"
                )
                val idTier = tierDAO().insert(tier)
                val operation = Operation(
                    montant = -200.20,
                    date_op = Calendar.getInstance().time,
                    fk_id_account = 1,
                    fk_id_tier = idTier,
                    fk_id_payment = idPay3
                )
                operationDao().insert(operation)
            }catch (pe: ParseException){

            }
        }
    }

    companion object{
        var INSTANCE: BankDatabase? = null
        fun getDatabase(context: Context): BankDatabase{
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context,BankDatabase::class.java, "bank.db").allowMainThreadQueries().build()
            }
            return INSTANCE!!
        }
    }
}