package spinnewyn.project.bank.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(indices = [Index(value=["id_rapprochement"], unique = true)])
data class Rapprochement(@PrimaryKey(autoGenerate = true) var id_rapprochement:Long? = null,
                         var date_rap: Date,
                         var solde:Double
                         ) {
    override fun equals(other: Any?): Boolean {
        if(this===other) return true
        if(javaClass != other?.javaClass) return false

        other as spinnewyn.project.bank.data.model.Rapprochement

        if(id_rapprochement != other.id_rapprochement) return false

        return true
    }

    override fun hashCode(): Int {
        return id_rapprochement?.hashCode() ?:0
    }

    override fun toString(): String {
        return "id rapprochement:$id_rapprochement, date:${date_rap}, solde:$solde"
    }
}