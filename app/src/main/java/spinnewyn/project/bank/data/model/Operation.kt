package spinnewyn.project.bank.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(indices = [Index(value= ["id_op"],unique = true), Index(value=["fk_id_account"]),Index(value = ["fk_id_tier"]), Index(value=["fk_id_payment"])],
    foreignKeys = [ForeignKey(entity = Account::class,
        parentColumns = ["id_account"],
        childColumns = ["fk_id_account"],
        onDelete = ForeignKey.CASCADE
        ),
    ForeignKey(entity = Tiers::class,
        parentColumns = ["id_tier"],
        childColumns = ["fk_id_tier"],
        onDelete = ForeignKey.NO_ACTION
        ),
    ForeignKey(entity = Payment::class,
        parentColumns = ["id_payment"],
        childColumns = ["fk_id_payment"],
        onDelete = ForeignKey.NO_ACTION),
    ForeignKey(entity = Rapprochement::class,
        parentColumns = ["id_rapprochement"],
        childColumns = ["fk_id_rapprochement"],
        onDelete = ForeignKey.NO_ACTION)]
)
data class Operation(@PrimaryKey(autoGenerate = true) var id_op:Long? = null,
                     var date_op:Date,
                     var montant:Long,
                     var fk_id_tier:Long? = null,
                     var fk_id_account:Long,
                     var fk_id_payment:Long? = null,
                     var fk_id_rapprochement:Long? = null,
                     var statut:Int? = null
                     ) {

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as spinnewyn.project.bank.data.model.Operation

        if(id_op != other.id_op) return false

        return true
    }

    override fun hashCode(): Int {
        return id_op?.hashCode() ?:0
    }

}