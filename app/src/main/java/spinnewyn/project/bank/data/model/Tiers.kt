package spinnewyn.project.bank.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(indices = [Index(value = ["id_tier"], unique = true)])
data class Tiers(@PrimaryKey(autoGenerate = true) var id_tier:Long? = null,
                 var tier_name:String
                 ): Serializable {
    override fun equals(other: Any?): Boolean {
        if(this===other) return true
        if(javaClass != other?.javaClass) return false

        other as spinnewyn.project.bank.data.model.Tiers

        if(id_tier != other.id_tier) return false

        return true
    }

    override fun hashCode(): Int {
        return id_tier?.hashCode()?:0
    }

    override fun toString(): String {
        return "id tier:$id_tier, tier name:$tier_name"
    }
}