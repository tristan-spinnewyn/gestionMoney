package spinnewyn.project.bank.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(indices = [Index(value=["id_account"], unique = true)])
data class Account(@PrimaryKey(autoGenerate = true) var id_account:Long? = null,
                   var nameAccount:String,
                   var soldeInit: Double = 100.00) : Serializable {
    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as spinnewyn.project.bank.data.model.Account

        if(id_account != other.id_account) return false

        return true
    }

    override fun hashCode(): Int {
        return id_account?.hashCode() ?:0
    }

    override fun toString(): String {
        return "id:$id_account, account: $nameAccount"
    }
}