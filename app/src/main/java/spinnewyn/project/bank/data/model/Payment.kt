package spinnewyn.project.bank.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value=["id_payment"], unique = true)])
data class Payment(@PrimaryKey(autoGenerate = true) var id_payment:Long? = null,
                   var name_payment:String) {
    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as spinnewyn.project.bank.data.model.Payment

        if(id_payment!= other.id_payment) return false

        return true
    }

    override fun hashCode(): Int {
        return id_payment?.hashCode() ?:0
    }

    override fun toString(): String {
        return "id payment:$id_payment, name_payment:$name_payment"
    }
}