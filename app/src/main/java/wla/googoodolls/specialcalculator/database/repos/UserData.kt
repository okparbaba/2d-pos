package wla.googoodolls.specialcalculator.database.repos

import androidx.room.*
import java.io.Serializable

@Entity
class UserData:Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cell_id")
    var cell_id = 0

    @ColumnInfo(name = "user_id")
    var user_id: Int? = 0

    @ColumnInfo(name = "cell")
    var cell:String? = ""

    @ColumnInfo(name = "amount")
    var amount:String? = ""

}