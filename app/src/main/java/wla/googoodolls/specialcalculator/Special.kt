package wla.googoodolls.specialcalculator

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class Customer(
    @PrimaryKey(autoGenerate = true)
    var cid: Int = 0,
    @ColumnInfo(name = "cname")
    var cname: String? = null,
    @ColumnInfo(name = "regdate")
    var regdate: Date? = null

)
@Entity
data class Number(
    @PrimaryKey(autoGenerate = true)
    var nid: Int = 0,
    @ColumnInfo(name = "num")
    var num: Int = 0
)

@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Customer::class,
            parentColumns = arrayOf("cid"),
            childColumns = arrayOf("cusid"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Number::class,
            parentColumns = arrayOf("nid"),
            childColumns = arrayOf("numid"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    )
)
data class Amount(
    @PrimaryKey(autoGenerate = true)
    var cnid: Int = 0,
    var cusid: Int = 0,
    var numid: Int = 0,
    var amo: Int = 0
)