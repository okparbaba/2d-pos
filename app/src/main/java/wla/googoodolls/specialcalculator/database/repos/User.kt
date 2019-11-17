package wla.googoodolls.specialcalculator.database.repos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
class User :Serializable{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Int = 0

    @ColumnInfo(name = "name")
    var name:String? = ""

    @ColumnInfo(name = "date")
    var date:String? = null

    @ColumnInfo(name = "debt_status")
    var debt_status:Boolean? = false

    //false is 2d and true is 3d
    @ColumnInfo(name = "lottery_status")
    var lottery_status:Boolean? = false
}